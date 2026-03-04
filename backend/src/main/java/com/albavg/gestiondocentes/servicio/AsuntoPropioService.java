package com.albavg.gestiondocentes.servicio;

import com.albavg.gestiondocentes.modelo.AsuntoPropio;
import com.albavg.gestiondocentes.modelo.Docente;
import com.albavg.gestiondocentes.repositorio.AsuntoPropioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AsuntoPropioService {

    @Autowired
    private AsuntoPropioRepository asuntoPropioRepository;

    @Autowired
    private DocenteService docenteService;

    public boolean solicitarDiaPropio(Long docenteId, LocalDate fecha, String descripcion) {
        Docente docente = docenteService.obtenerPorId(docenteId).orElse(null);
        if (docente == null) return false;
        if (fecha.isBefore(LocalDate.now())) return false;
        if (asuntoPropioRepository.existsByDocenteIdAndDiaSolicitado(docenteId, fecha)) return false;

        AsuntoPropio asuntoPropio = new AsuntoPropio();
        asuntoPropio.setDiaSolicitado(fecha);
        asuntoPropio.setDescripcion(descripcion);
        asuntoPropio.setAprobado(false);
        asuntoPropio.setDocente(docente);
        asuntoPropioRepository.save(asuntoPropio);
        return true;
    }

    public boolean validarDiaPropio(Long asuntoPropioId, boolean aceptado) {
        AsuntoPropio asuntoPropio = asuntoPropioRepository.findById(asuntoPropioId).orElse(null);
        if (asuntoPropio == null) return false;

        asuntoPropio.setAprobado(aceptado);
        asuntoPropio.setFechaTramitacion(LocalDate.now());
        asuntoPropioRepository.save(asuntoPropio);
        return true;
    }

    public List<AsuntoPropio> consultarDiasPropios(Long docenteId) {
        return asuntoPropioRepository.findByDocenteId(docenteId);
    }

    public List<AsuntoPropio> obtenerDiasPendientesDisfrutar(Long docenteId) {
        return asuntoPropioRepository.findByDocenteIdAndAprobadoTrueAndDiaSolicitadoGreaterThanEqual(
            docenteId, LocalDate.now());
    }

    public Docente obtenerDocenteConMasDiasDisfrutados() {
        List<AsuntoPropio> asuntosPropios = asuntoPropioRepository.findAllAprobadosYDisfrutados(LocalDate.now());
        if (asuntosPropios.isEmpty()) return null;

        Map<Long, Long> conteo = new HashMap<>();
        Map<Long, Docente> docentes = new HashMap<>();

        for (AsuntoPropio asunto : asuntosPropios) {
            Long docenteId = asunto.getDocente().getId();
            conteo.put(docenteId, conteo.getOrDefault(docenteId, 0L) + 1);
            docentes.put(docenteId, asunto.getDocente());
        }

        Docente docenteConMasDias = null;
        long maxDias = 0;
        for (Map.Entry<Long, Long> entry : conteo.entrySet()) {
            if (entry.getValue() > maxDias) {
                maxDias = entry.getValue();
                docenteConMasDias = docentes.get(entry.getKey());
            }
        }
        return docenteConMasDias;
    }
}
