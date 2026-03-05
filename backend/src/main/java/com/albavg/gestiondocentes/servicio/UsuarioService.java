package com.albavg.gestiondocentes.servicio;

import com.albavg.gestiondocentes.dto.RegistroRequest;
import com.albavg.gestiondocentes.dto.RegistroResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioService extends UserDetailsService {
    RegistroResponse registrar(RegistroRequest request);
}
