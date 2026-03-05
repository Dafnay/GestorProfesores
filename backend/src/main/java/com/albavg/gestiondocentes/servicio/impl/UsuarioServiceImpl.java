package com.albavg.gestiondocentes.servicio.impl;

import com.albavg.gestiondocentes.dto.RegistroRequest;
import com.albavg.gestiondocentes.dto.RegistroResponse;
import com.albavg.gestiondocentes.modelo.UserRole;
import com.albavg.gestiondocentes.modelo.Usuario;
import com.albavg.gestiondocentes.repositorio.UsuarioRepository;
import com.albavg.gestiondocentes.servicio.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegistroResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        UserRole role = request.role() != null ? request.role() : UserRole.DOCENTE;

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();

        Usuario saved = usuarioRepository.save(usuario);
        return new RegistroResponse(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRole());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
