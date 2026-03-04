package com.albavg.gestiondocentes.repositorio;

import com.albavg.gestiondocentes.modelo.AsuntoPropio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsuntoPropioRepository extends JpaRepository<AsuntoPropio, Long> {

    List<AsuntoPropio> findByDocenteId(Long docenteId);

    List<AsuntoPropio> findByDocenteIdAndAprobadoTrueAndDiaSolicitadoGreaterThanEqual(
        Long docenteId, LocalDate fecha);

    boolean existsByDocenteIdAndDiaSolicitado(Long docenteId, LocalDate fecha);

    Long countByDocenteIdAndAprobadoTrueAndDiaSolicitadoLessThan(Long docenteId, LocalDate fecha);

    @Query("SELECT a FROM AsuntoPropio a WHERE a.aprobado = true AND a.diaSolicitado < :fecha ORDER BY a.docente.id")
    List<AsuntoPropio> findAllAprobadosYDisfrutados(@Param("fecha") LocalDate fecha);
}
