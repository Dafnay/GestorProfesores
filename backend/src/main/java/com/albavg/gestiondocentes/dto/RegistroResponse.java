package com.albavg.gestiondocentes.dto;

import com.albavg.gestiondocentes.modelo.UserRole;

public record RegistroResponse(Long id, String username, String email, UserRole role) {
}
