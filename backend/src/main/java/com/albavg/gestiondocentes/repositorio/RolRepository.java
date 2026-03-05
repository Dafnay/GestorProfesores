package com.albavg.gestiondocentes.repositorio;

import com.albavg.gestiondocentes.modelo.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
}
