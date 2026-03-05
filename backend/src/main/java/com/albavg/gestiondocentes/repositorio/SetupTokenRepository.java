package com.albavg.gestiondocentes.repositorio;

import com.albavg.gestiondocentes.modelo.SetupToken;
import com.albavg.gestiondocentes.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SetupTokenRepository extends JpaRepository<SetupToken, Long> {
    Optional<SetupToken> findByToken(String token);
    void deleteByUsuario(Usuario usuario);
}
