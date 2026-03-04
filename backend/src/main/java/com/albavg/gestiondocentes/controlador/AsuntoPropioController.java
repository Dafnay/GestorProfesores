package com.albavg.gestiondocentes.controlador;

import com.albavg.gestiondocentes.modelo.AsuntoPropio;
import com.albavg.gestiondocentes.modelo.Docente;
import com.albavg.gestiondocentes.servicio.AsuntoPropioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/asuntospropios")
public class AsuntoPropioController {

    @Autowired
    private AsuntoPropioService asuntoPropioService;

    @PostMapping("/solicitar")
    public boolean solicitarDiaPropio(@RequestParam Long docenteId,
                                      @RequestParam String fecha,
                                      @RequestParam(required = false) String descripcion) {
        return asuntoPropioService.solicitarDiaPropio(docenteId, LocalDate.parse(fecha), descripcion);
    }

    @PutMapping("/validar/{id}")
    public boolean validarDiaPropio(@PathVariable Long id,
                                    @RequestParam boolean aceptado) {
        return asuntoPropioService.validarDiaPropio(id, aceptado);
    }

    @GetMapping("/consultar/{docenteId}")
    public List<AsuntoPropio> consultarDiasPropios(@PathVariable Long docenteId) {
        return asuntoPropioService.consultarDiasPropios(docenteId);
    }

    @GetMapping("/pendientes/{docenteId}")
    public List<AsuntoPropio> obtenerDiasPendientesDisfrutar(@PathVariable Long docenteId) {
        return asuntoPropioService.obtenerDiasPendientesDisfrutar(docenteId);
    }

    @GetMapping("/docente-mas-dias")
    public Docente obtenerDocenteConMasDiasDisfrutados() {
        return asuntoPropioService.obtenerDocenteConMasDiasDisfrutados();
    }
}
