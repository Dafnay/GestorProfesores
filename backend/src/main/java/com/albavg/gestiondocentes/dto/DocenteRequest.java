package com.albavg.gestiondocentes.dto;

public record DocenteRequest(
        String nombre,
        String apellidos,
        String email,
        String siglas,
        Long departamentoId,
        Long rolId
) {}
