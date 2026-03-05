package com.albavg.gestiondocentes.dto;

import com.albavg.gestiondocentes.modelo.UserRole;

public record RegistroRequest(String username, String email, String password, UserRole role) {
}
