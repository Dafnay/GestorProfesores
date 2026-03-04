package com.albavg.gestiondocentes.controlador;

import com.albavg.gestiondocentes.servicio.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping("/contar/{codigo}")
    public Long contarProfesoresPorCodigo(@PathVariable String codigo) {
        return departamentoService.contarProfesoresPorCodigo(codigo);
    }
}
