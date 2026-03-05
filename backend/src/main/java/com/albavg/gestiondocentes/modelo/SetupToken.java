package com.albavg.gestiondocentes.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class SetupToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime expiry;

    public SetupToken(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
        this.expiry = LocalDateTime.now().plusHours(24);
    }

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(expiry);
    }
}
