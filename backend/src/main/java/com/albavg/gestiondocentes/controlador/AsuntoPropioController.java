package com.albavg.gestiondocentes.controlador;

import com.albavg.gestiondocentes.modelo.AsuntoPropio;
import com.albavg.gestiondocentes.modelo.Docente;
import com.albavg.gestiondocentes.servicio.AsuntoPropioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/asuntospropios")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Asuntos Propios", description = "Gestión de días de asuntos propios de los docentes")
public class AsuntoPropioController {

    @Autowired
    private AsuntoPropioService asuntoPropioService;

    @Operation(
            summary = "Solicitar un día de asunto propio",
            description = "El docente solicita un día de asunto propio indicando la fecha y una descripción opcional."
    )
    @ApiResponse(responseCode = "200", description = "true si la solicitud fue creada correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    @PostMapping("/solicitar")
    public boolean solicitarDiaPropio(
            @Parameter(description = "ID del docente") @RequestParam Long docenteId,
            @Parameter(description = "Fecha solicitada (yyyy-MM-dd)") @RequestParam String fecha,
            @Parameter(description = "Descripción opcional") @RequestParam(required = false) String descripcion) {
        return asuntoPropioService.solicitarDiaPropio(docenteId, LocalDate.parse(fecha), descripcion);
    }

    @Operation(
            summary = "Validar o rechazar una solicitud",
            description = "El administrador acepta o rechaza la solicitud de asunto propio."
    )
    @ApiResponse(responseCode = "200", description = "true si la operación fue exitosa",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    @PutMapping("/validar/{id}")
    public boolean validarDiaPropio(
            @Parameter(description = "ID de la solicitud") @PathVariable Long id,
            @Parameter(description = "true para aceptar, false para rechazar") @RequestParam boolean aceptado) {
        return asuntoPropioService.validarDiaPropio(id, aceptado);
    }

    @Operation(summary = "Consultar todas las solicitudes de un docente")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes del docente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AsuntoPropio.class)))
    @GetMapping("/consultar/{docenteId}")
    public List<AsuntoPropio> consultarDiasPropios(
            @Parameter(description = "ID del docente") @PathVariable Long docenteId) {
        return asuntoPropioService.consultarDiasPropios(docenteId);
    }

    @Operation(summary = "Obtener días aprobados pendientes de disfrutar")
    @ApiResponse(responseCode = "200", description = "Lista de días aprobados aún no disfrutados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AsuntoPropio.class)))
    @GetMapping("/pendientes/{docenteId}")
    public List<AsuntoPropio> obtenerDiasPendientesDisfrutar(
            @Parameter(description = "ID del docente") @PathVariable Long docenteId) {
        return asuntoPropioService.obtenerDiasPendientesDisfrutar(docenteId);
    }

    @Operation(summary = "Docente con más días de asuntos propios disfrutados")
    @ApiResponse(responseCode = "200", description = "Datos del docente con más días disfrutados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Docente.class)))
    @GetMapping("/docente-mas-dias")
    public Docente obtenerDocenteConMasDiasDisfrutados() {
        return asuntoPropioService.obtenerDocenteConMasDiasDisfrutados();
    }
}
