package com.albavg.gestiondocentes.servicio;

import com.albavg.gestiondocentes.dto.DepartamentoRequest;
import com.albavg.gestiondocentes.modelo.Departamento;
import com.albavg.gestiondocentes.repositorio.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Departamento> listarTodos() {
        return departamentoRepository.findAll();
    }

    public Long contarProfesoresPorCodigo(String codigo) {
        return departamentoRepository.contarDocentesPorCodigo(codigo);
    }

    public Departamento crear(DepartamentoRequest request) {
        Departamento d = new Departamento();
        d.setNombre(request.nombre());
        d.setCodigo(request.codigo());
        d.setTelefono(request.telefono());
        return departamentoRepository.save(d);
    }

    public Optional<Departamento> actualizar(Long id, DepartamentoRequest request) {
        return departamentoRepository.findById(id).map(d -> {
            d.setNombre(request.nombre());
            d.setCodigo(request.codigo());
            d.setTelefono(request.telefono());
            return departamentoRepository.save(d);
        });
    }

    public boolean eliminar(Long id) {
        if (departamentoRepository.findById(id).isEmpty()) return false;
        departamentoRepository.deleteById(id);
        return true;
    }
}
