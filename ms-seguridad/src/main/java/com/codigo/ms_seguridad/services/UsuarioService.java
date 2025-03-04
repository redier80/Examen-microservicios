package com.codigo.ms_seguridad.services;

import com.codigo.ms_seguridad.aggregates.request.UsuarioRequest;
import com.codigo.ms_seguridad.aggregates.response.UsuarioResponse;
import com.codigo.ms_seguridad.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsuarioService {
    UserDetailsService userDetailsService();
    List<Usuario> obtenertodos(String nombreRol);

    Usuario actualizarUsuario(Long id, UsuarioRequest usuarioRequest);
}
