package com.albavg.gestiondocentes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocenteDTO {

    private Long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String siglas;
    private String nombreDepartamento;
    private String codigoDepartamento;
    private String nombreRol;
}
