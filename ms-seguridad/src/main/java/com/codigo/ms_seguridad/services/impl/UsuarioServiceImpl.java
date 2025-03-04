package com.codigo.ms_seguridad.services.impl;

import com.codigo.ms_seguridad.aggregates.constants.Constants;
import com.codigo.ms_seguridad.aggregates.request.SignUpRequest;
import com.codigo.ms_seguridad.aggregates.request.UsuarioRequest;
import com.codigo.ms_seguridad.aggregates.response.UsuarioResponse;
import com.codigo.ms_seguridad.entity.Role;
import com.codigo.ms_seguridad.entity.Usuario;
import com.codigo.ms_seguridad.repository.UsuarioRepository;
import com.codigo.ms_seguridad.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return usuarioRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("usuario no encontrado en Base de datos"));
            }
        };
    }

    @Override
    public List<Usuario> obtenertodos(String nombreRol) {
        return usuarioRepository.findByRoles_NombreRol(nombreRol);
    }

    @Override
    public Usuario actualizarUsuario(Long id, UsuarioRequest usuarioRequest) {

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Error Usuario no encontrado")
        );
        usuario = Usuario.builder()
                .id(usuario.getId())
                .nombres(usuarioRequest.getNombres() != null ? usuarioRequest.getNombres() : usuario.getNombres())
                .apellidos(usuarioRequest.getApellidos() != null ? usuarioRequest.getApellidos() : usuario.getApellidos())
                .email(usuarioRequest.getEmail() != null ? usuarioRequest.getEmail() : usuario.getEmail())
                .username(usuarioRequest.getUsername() != null ? usuarioRequest.getUsername() : usuario.getUsername())
                .password(usuario.getPassword())
                .tipoDoc(usuarioRequest.getTipoDoc() != null ? usuarioRequest.getTipoDoc() : usuario.getTipoDoc())
                .numDoc(usuarioRequest.getNumDoc() != null ? usuarioRequest.getNumDoc() : usuario.getNumDoc())
                .isAccountNonExpired(usuario.getIsAccountNonExpired())
                .isAccountNonLocked(usuario.getIsAccountNonLocked())
                .isCredentialsNonExpired(usuario.getIsCredentialsNonExpired())
                .isEnabled(usuario.getIsEnabled())
                .roles(usuario.getRoles()) // Mantiene los roles sin cambios
                .build();
        return usuarioRepository.save(usuario);

    }

}
