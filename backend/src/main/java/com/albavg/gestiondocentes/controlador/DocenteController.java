package com.albavg.gestiondocentes.controlador;

import com.albavg.gestiondocentes.modelo.Docente;
import com.albavg.gestiondocentes.servicio.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/docentes")
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @GetMapping("/{id}")
    public Docente obtenerDocentePorId(@PathVariable Long id) {
        return docenteService.obtenerPorId(id).orElse(null);
    }

    @GetMapping("/ordenados")
    public List<Docente> obtenerDocentesOrdenados() {
        return docenteService.obtenerDocentesOrdenadosPorApellidos();
    }

    @GetMapping("/departamento/{nombre}")
    public List<Docente> obtenerDocentesPorDepartamento(@PathVariable String nombre) {
        return docenteService.obtenerDocentesPorDepartamento(nombre);
    }
}
