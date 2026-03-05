package com.albavg.gestiondocentes.config;

import com.albavg.gestiondocentes.modelo.UserRole;
import com.albavg.gestiondocentes.modelo.Usuario;
import com.albavg.gestiondocentes.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByUsername("Admin")) {
            Usuario admin = Usuario.builder()
                    .username("Admin")
                    .email("admin@prueba.es")
                    .password(passwordEncoder.encode("admin123456"))
                    .role(UserRole.ADMIN)
                    .build();
            usuarioRepository.save(admin);
            System.out.println("Usuario Admin creado correctamente.");
        }
    }
}
