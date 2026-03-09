package com.albavg.gestiondocentes.repositorio;

import com.albavg.gestiondocentes.modelo.Docente;
import com.albavg.gestiondocentes.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    List<Docente> findAllByOrderByApellidosAsc();

    List<Docente> findByDepartamentoNombre(String nombreDepartamento);

    Optional<Docente> findByUsuario(Usuario usuario);
}
