package com.albavg.gestiondocentes.controlador;

import com.albavg.gestiondocentes.dto.DepartamentoRequest;
import com.albavg.gestiondocentes.modelo.Departamento;
import com.albavg.gestiondocentes.servicio.DepartamentoService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Departamentos", description = "Gestión de departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @Operation(summary = "Listar todos los departamentos")
    @ApiResponse(responseCode = "200", description = "Lista de departamentos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class)))
    @GetMapping
    public List<Departamento> listarTodos() {
        return departamentoService.listarTodos();
    }

    @Operation(summary = "Contar docentes de un departamento por código")
    @ApiResponse(responseCode = "200", description = "Número de docentes del departamento",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    @GetMapping("/contar/{codigo}")
    public Long contarProfesoresPorCodigo(
            @Parameter(description = "Código del departamento") @PathVariable String codigo) {
        return departamentoService.contarProfesoresPorCodigo(codigo);
    }

    @Operation(summary = "Crear un nuevo departamento (solo ADMIN)")
    @ApiResponse(responseCode = "201", description = "Departamento creado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class)))

    @PostMapping
    public ResponseEntity<Departamento> crear(@RequestBody DepartamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.crear(request));
    }

    @Operation(summary = "Actualizar un departamento existente (solo ADMIN)")
    @ApiResponse(responseCode = "200", description = "Departamento actualizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Departamento.class)))
    @ApiResponse(responseCode = "404", description = "Departamento no encontrado", content = @Content)

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> actualizar(
            @Parameter(description = "ID del departamento") @PathVariable Long id,
            @RequestBody DepartamentoRequest request) {
        return departamentoService.actualizar(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un departamento (solo ADMIN, sin docentes asignados)")
    @ApiResponse(responseCode = "204", description = "Departamento eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Departamento no encontrado", content = @Content)
    @ApiResponse(responseCode = "409", description = "El departamento tiene docentes asignados y no puede eliminarse", content = @Content)

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del departamento") @PathVariable Long id) {
        Departamento d = departamentoService.listarTodos().stream()
                .filter(dep -> dep.getId().equals(id))
                .findFirst().orElse(null);
        if (d == null) return ResponseEntity.notFound().build();
        long docentes = departamentoService.contarProfesoresPorCodigo(d.getCodigo());
        if (docentes > 0) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        departamentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
