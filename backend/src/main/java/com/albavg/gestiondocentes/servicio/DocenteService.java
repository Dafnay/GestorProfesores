package com.albavg.gestiondocentes.servicio;

import com.albavg.gestiondocentes.modelo.Docente;
import com.albavg.gestiondocentes.repositorio.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    public Optional<Docente> obtenerPorId(Long id) {
        return docenteRepository.findById(id);
    }

    public List<Docente> obtenerDocentesOrdenadosPorApellidos() {
        return docenteRepository.findAllByOrderByApellidosAsc();
    }

    public List<Docente> obtenerDocentesPorDepartamento(String nombreDepartamento) {
        return docenteRepository.findByDepartamentoNombre(nombreDepartamento);
    }
}
