package com.albavg.gestiondocentes.repositorio;

import com.albavg.gestiondocentes.modelo.SetupToken;
import com.albavg.gestiondocentes.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SetupTokenRepository extends JpaRepository<SetupToken, Long> {
    Optional<SetupToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM SetupToken t WHERE t.usuario = :usuario")
    void deleteByUsuario(@Param("usuario") Usuario usuario);
}
