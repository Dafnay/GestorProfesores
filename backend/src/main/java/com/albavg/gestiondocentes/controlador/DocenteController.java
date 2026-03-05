package com.albavg.gestiondocentes.controlador;

import com.albavg.gestiondocentes.dto.DocenteRequest;
import com.albavg.gestiondocentes.modelo.Docente;
import com.albavg.gestiondocentes.servicio.DocenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/docentes")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Docentes", description = "Consulta de docentes")
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @Operation(summary = "Obtener docente por ID")
    @ApiResponse(responseCode = "200", description = "Docente encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Docente.class)))
    @ApiResponse(responseCode = "404", description = "Docente no encontrado", content = @Content)
    @GetMapping("/{id}")
    public Docente obtenerDocentePorId(
            @Parameter(description = "ID del docente") @PathVariable Long id) {
        return docenteService.obtenerPorId(id).orElse(null);
    }

    @Operation(summary = "Listar docentes ordenados por apellidos")
    @ApiResponse(responseCode = "200", description = "Lista de docentes ordenada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Docente.class)))
    @GetMapping("/ordenados")
    public List<Docente> obtenerDocentesOrdenados() {
        return docenteService.obtenerDocentesOrdenadosPorApellidos();
    }

    @Operation(summary = "Obtener docentes de un departamento")
    @ApiResponse(responseCode = "200", description = "Lista de docentes del departamento",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Docente.class)))
    @GetMapping("/departamento/{nombre}")
    public List<Docente> obtenerDocentesPorDepartamento(
            @Parameter(description = "Nombre del departamento") @PathVariable String nombre) {
        return docenteService.obtenerDocentesPorDepartamento(nombre);
    }

    @Operation(summary = "Crear un nuevo docente (solo ADMIN)")
    @ApiResponse(responseCode = "201", description = "Docente creado correctamente")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Docente> crear(@RequestBody DocenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(docenteService.guardar(request));
    }
}
