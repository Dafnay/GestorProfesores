package com.albavg.gestiondocentes.controlador;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Departamentos", description = "Consulta de departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @Operation(summary = "Listar todos los departamentos")
    @ApiResponse(responseCode = "200", description = "Lista de departamentos")
    @GetMapping
    public List<Departamento> listarTodos() {
        return departamentoService.listarTodos();
    }

    @Operation(summary = "Contar profesores de un departamento por código")
    @ApiResponse(responseCode = "200", description = "Número de profesores del departamento",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    @GetMapping("/contar/{codigo}")
    public Long contarProfesoresPorCodigo(
            @Parameter(description = "Código del departamento") @PathVariable String codigo) {
        return departamentoService.contarProfesoresPorCodigo(codigo);
    }
}
