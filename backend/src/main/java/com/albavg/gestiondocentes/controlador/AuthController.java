package com.albavg.gestiondocentes.controlador;

import com.albavg.gestiondocentes.dto.*;
import com.albavg.gestiondocentes.servicio.EmailService;
import com.albavg.gestiondocentes.modelo.SetupToken;
import com.albavg.gestiondocentes.modelo.Usuario;
import com.albavg.gestiondocentes.repositorio.DocenteRepository;
import com.albavg.gestiondocentes.repositorio.SetupTokenRepository;
import com.albavg.gestiondocentes.repositorio.UsuarioRepository;
import com.albavg.gestiondocentes.security.JwtUtil;
import com.albavg.gestiondocentes.servicio.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.UUID;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro e inicio de sesión")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SetupTokenRepository setupTokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un usuario con rol DOCENTE por defecto. El administrador puede indicar rol ADMIN."
    )
    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistroResponse.class),
                    examples = @ExampleObject("""
                            {
                              "id": 1,
                              "username": "jlopez",
                              "email": "jlopez@centro.es",
                              "role": "DOCENTE"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Usuario o email ya en uso")
    @PostMapping("/register")
    public ResponseEntity<RegistroResponse> registrar(@RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(request));
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y devuelve un token JWT para usar en el header Authorization."
    )
    @ApiResponse(responseCode = "200", description = "Login correcto",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponse.class),
                    examples = @ExampleObject("""
                            {
                              "token": "eyJhbGciOiJIUzI1NiJ9...",
                              "username": "jlopez",
                              "role": "DOCENTE"
                            }
                            """)))
    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        Usuario usuarioPorEmail = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Credenciales incorrectas"));

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioPorEmail.getUsername(), request.password())
        );

        Usuario usuario = (Usuario) auth.getPrincipal();
        String token = jwtUtil.generateToken(usuario.getUsername(), "ROLE_" + usuario.getRole().name());
        String nombre = null;
        String apellidos = null;
        var docente = docenteRepository.findByUsuario(usuario);
        if (docente.isPresent()) {
            nombre = docente.get().getNombre();
            apellidos = docente.get().getApellidos();
        }
        return ResponseEntity.ok(new JwtResponse(token, usuario.getUsername(), usuario.getRole(), nombre, apellidos));
    }

    @Operation(summary = "Configurar contraseña inicial mediante token de email")
    @ApiResponse(responseCode = "200", description = "Contraseña configurada correctamente")
    @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    @PostMapping("/setup-password")
    public ResponseEntity<Void> setupPassword(@RequestBody SetupPasswordRequest request) {
        SetupToken setupToken = setupTokenRepository.findByToken(request.token())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (setupToken.isExpirado()) {
            setupTokenRepository.delete(setupToken);
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = setupToken.getUsuario();
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuarioRepository.save(usuario);
        setupTokenRepository.delete(setupToken);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Solicitar restablecimiento de contraseña",
            description = "Envía un email con enlace para restablecer la contraseña. Siempre responde 200 para no revelar si el email existe.")
    @ApiResponse(responseCode = "200", description = "Email enviado si el usuario existe")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        usuarioRepository.findByEmail(request.email()).ifPresent(usuario -> {
            setupTokenRepository.deleteByUsuario(usuario);
            String token = UUID.randomUUID().toString();
            setupTokenRepository.save(new SetupToken(token, usuario));
            emailService.enviarResetPassword(usuario.getEmail(), usuario.getUsername(), token);
        });
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cambiar contraseña (usuario autenticado)")
    @ApiResponse(responseCode = "200", description = "Contraseña cambiada correctamente")
    @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta")
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            org.springframework.security.core.Authentication authentication,
            @RequestBody ChangePasswordRequest request) {

        Usuario usuario = usuarioRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.passwordActual(), usuario.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        usuario.setPassword(passwordEncoder.encode(request.passwordNuevo()));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok().build();
    }
}
