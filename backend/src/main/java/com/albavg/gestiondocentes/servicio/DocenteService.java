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

        emailService.enviarSetupPassword(request.email(), request.nombre(), token);

        return saved;
    }
}
