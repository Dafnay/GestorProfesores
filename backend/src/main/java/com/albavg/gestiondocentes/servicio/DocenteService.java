package com.albavg.gestiondocentes.servicio;

import com.albavg.gestiondocentes.dto.DocenteRequest;
import com.albavg.gestiondocentes.modelo.*;
import com.albavg.gestiondocentes.repositorio.DocenteRepository;
import com.albavg.gestiondocentes.repositorio.SetupTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private SetupTokenRepository setupTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public Optional<Docente> obtenerPorId(Long id) {
        return docenteRepository.findById(id);
    }

    public List<Docente> obtenerDocentesOrdenadosPorApellidos() {
        return docenteRepository.findAllByOrderByApellidosAsc();
    }

    public List<Docente> obtenerDocentesPorDepartamento(String nombreDepartamento) {
        return docenteRepository.findByDepartamentoNombre(nombreDepartamento);
    }

    @Transactional
    public Optional<Docente> actualizar(Long id, DocenteRequest request) {
        return docenteRepository.findById(id).map(docente -> {
            docente.setNombre(request.nombre());
            docente.setApellidos(request.apellidos());
            docente.setEmail(request.email());
            docente.setSiglas(request.siglas());
            if (request.departamentoId() != null) {
                Departamento dep = new Departamento();
                dep.setId(request.departamentoId());
                docente.setDepartamento(dep);
            } else {
                docente.setDepartamento(null);
            }
            if (request.rolId() != null) {
                Rol rol = new Rol();
                rol.setId(request.rolId());
                docente.setRol(rol);
            } else {
                docente.setRol(null);
            }
            if (docente.getUsuario() != null) {
                docente.getUsuario().setEmail(request.email());
                docente.getUsuario().setUsername(request.siglas());
            }
            return docenteRepository.save(docente);
        });
    }

    @Transactional
    public boolean eliminar(Long id) {
        return docenteRepository.findById(id).map(docente -> {
            if (docente.getUsuario() != null) {
                setupTokenRepository.deleteByUsuario(docente.getUsuario());
            }
            docenteRepository.delete(docente);
            return true;
        }).orElse(false);
    }

    @Transactional
    public Docente guardar(DocenteRequest request) {
        Docente docente = new Docente();
        docente.setNombre(request.nombre());
        docente.setApellidos(request.apellidos());
        docente.setEmail(request.email());
        docente.setSiglas(request.siglas());
        if (request.departamentoId() != null) {
            Departamento dep = new Departamento();
            dep.setId(request.departamentoId());
            docente.setDepartamento(dep);
        }
        if (request.rolId() != null) {
            Rol rol = new Rol();
            rol.setId(request.rolId());
            docente.setRol(rol);
        }

        Usuario usuario = Usuario.builder()
                .username(request.siglas())
                .email(request.email())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .role(UserRole.DOCENTE)
                .build();
        docente.setUsuario(usuario);

        Docente saved = docenteRepository.save(docente);

        String token = UUID.randomUUID().toString();
        setupTokenRepository.save(new SetupToken(token, saved.getUsuario()));

        emailService.enviarSetupPassword(request.email(), request.nombre() + " " + request.apellidos(), token);

        return saved;
    }
}
