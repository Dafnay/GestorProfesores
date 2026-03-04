package com.albavg.gestiondocentes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsuntoPropioDetalleDTO {

    private Long asuntoPropioId;
    private LocalDate diaSolicitado;
    private LocalDate fechaTramitacion;
    private String descripcion;
    private Boolean aprobado;

    private Long docenteId;
    private String nombreDocente;
    private String apellidosDocente;
    private String emailDocente;
    private String siglasDocente;

    private String nombreDepartamento;
    private String codigoDepartamento;

    private String nombreRol;
    private Integer ordenRol;
}
