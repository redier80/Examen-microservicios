package com.codigo.ms_seguridad.repository;

import com.codigo.ms_seguridad.aggregates.response.UsuarioResponse;
import com.codigo.ms_seguridad.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    List<Usuario> findByRoles_NombreRol(String nombreRol);
}
