package com.albavg.gestiondocentes.controlador;

import com.albavg.gestiondocentes.modelo.Rol;
import com.albavg.gestiondocentes.repositorio.RolRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Roles", description = "Consulta de roles de docentes")
public class RolController {

    @Autowired
    private RolRepository rolRepository;

    @Operation(summary = "Listar todos los roles")
    @GetMapping
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }
}
