package com.codigo.ms_seguridad.services.impl;

import com.codigo.ms_seguridad.aggregates.constants.Constants;
import com.codigo.ms_seguridad.aggregates.request.SignInRequest;
import com.codigo.ms_seguridad.aggregates.request.SignUpRequest;
import com.codigo.ms_seguridad.aggregates.request.ValidateTokenRequest;
import com.codigo.ms_seguridad.aggregates.response.SignInResponse;
import com.codigo.ms_seguridad.aggregates.response.UsuarioResponse;
import com.codigo.ms_seguridad.entity.Rol;
import com.codigo.ms_seguridad.entity.Role;
import com.codigo.ms_seguridad.entity.Usuario;
import com.codigo.ms_seguridad.repository.RolRepository;
import com.codigo.ms_seguridad.repository.UsuarioRepository;
import com.codigo.ms_seguridad.services.AuthenticationService;
import com.codigo.ms_seguridad.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Override
    public Usuario signUpUser(SignUpRequest signUpRequest) {
        Usuario usuario = getUsuarioEntity(signUpRequest);
        usuario.setRoles(Collections.singleton(getRoles(Role.USER)));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario signUpAdmin(SignUpRequest signUpRequest) {
        Usuario usuario = getUsuarioEntity(signUpRequest);
        usuario.setRoles(Collections.singleton(getRoles(Role.ADMIN)));
        return usuarioRepository.save(usuario);
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getUsername(),signInRequest.getPassword()
        ));
        var user = usuarioRepository.findByUsername(signInRequest.getUsername()).orElseThrow(
                ()-> new UsernameNotFoundException("Error usuario no encontrado")
        );
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);
        return SignInResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public SignInResponse getTokenByRefreshToken(String refreshToken) throws IllegalAccessException {
        //VALIDAR QUE SEA UN REFRESHTOKEN
        log.info("Ejecutando - getTokenByRefreshToken");
        if(!jwtService.isRefreshToken(refreshToken)){
            throw new RuntimeException("Error el token ingresado no es un REFRESH ");
        }
        //EXTRAER EL USUARIO
        String userName = jwtService.extractUserName(refreshToken);

        //BUSCAMOS AL USUARIO EN BD
        Usuario usuario = usuarioRepository.findByUsername(userName).orElseThrow(
                ()-> new UsernameNotFoundException("Error usuario no encontrado"));
        //VALIDAR QUE EL REFRESHTOKEN LE PERTENEZCA A UN USUARIO
        if(!jwtService.validateToken(refreshToken, usuario)){
            throw new IllegalAccessException("Error el token no le pertenece a al usuario");
        }
        //GENERAR EL ACCESSTOKEN
        String newToken = jwtService.generateToken(usuario);
        return SignInResponse.builder()
                .token(newToken)
                .refreshToken(refreshToken)
                .build();
    }

        @Override
    public UsuarioResponse getUserInfo() {
        Usuario usuario = new Usuario();
        // Obtener la autenticación del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        // Si hay usuario autenticado,
        if (authentication != null || authentication.isAuthenticated()) {
            usuario=  usuarioRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));;
        }
        UsuarioResponse usuarioResponse = UsuarioResponse.builder()
                .id(usuario.getId())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .email(usuario.getEmail())
                .numDoc(usuario.getNumDoc())
                .tipoDoc(usuario.getTipoDoc())
                .username(usuario.getUsername())
                .build();
        return usuarioResponse;
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken2(token);
    }

    private Usuario getUsuarioEntity(SignUpRequest signUpRequest){
        return Usuario.builder()
                .nombres(signUpRequest.getNombres())
                .apellidos(signUpRequest.getApellidos())
                .email(signUpRequest.getEmail())
                .username(signUpRequest.getUsername())
                .password(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()))
                .tipoDoc(signUpRequest.getTipoDoc())
                .numDoc(signUpRequest.getNumDoc())
                .isAccountNonExpired(Constants.STATUS_ACTIVE)
                .isAccountNonLocked(Constants.STATUS_ACTIVE)
                .isCredentialsNonExpired(Constants.STATUS_ACTIVE)
                .isEnabled(Constants.STATUS_ACTIVE)
                .build();
    }
    private Rol getRoles(Role rolBuscado){
        return rolRepository.findByNombreRol(rolBuscado.name())
                .orElseThrow(() -> new RuntimeException("Error el rol no existe: " + rolBuscado.name()));
    }
}
