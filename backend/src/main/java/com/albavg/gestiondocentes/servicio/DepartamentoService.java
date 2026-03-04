package com.albavg.gestiondocentes.servicio;

import com.albavg.gestiondocentes.repositorio.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public Long contarProfesoresPorCodigo(String codigo) {
        return departamentoRepository.contarDocentesPorCodigo(codigo);
    }
}
