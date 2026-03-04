package com.albavg.gestiondocentes.repositorio;

import com.albavg.gestiondocentes.modelo.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    @Query("SELECT COUNT(d) FROM Docente d WHERE d.departamento.codigo = :codigo")
    Long contarDocentesPorCodigo(@Param("codigo") String codigo);
}
