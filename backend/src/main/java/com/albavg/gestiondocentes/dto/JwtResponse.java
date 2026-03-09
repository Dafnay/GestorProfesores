package com.albavg.gestiondocentes.dto;

import com.albavg.gestiondocentes.modelo.UserRole;

public record JwtResponse(String token, String username, UserRole role, String nombre, String apellidos) {
}
