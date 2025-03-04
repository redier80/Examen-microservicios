package com.codigo.ms_seguridad.services;

import com.codigo.ms_seguridad.aggregates.request.SignInRequest;
import com.codigo.ms_seguridad.aggregates.request.SignUpRequest;
import com.codigo.ms_seguridad.aggregates.request.ValidateTokenRequest;
import com.codigo.ms_seguridad.aggregates.response.SignInResponse;
import com.codigo.ms_seguridad.aggregates.response.UsuarioResponse;
import com.codigo.ms_seguridad.entity.Usuario;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface AuthenticationService {

    Usuario signUpUser(SignUpRequest signUpRequest);
    Usuario signUpAdmin(SignUpRequest signUpRequest);
    //PAra Login
    SignInResponse signIn(SignInRequest signInRequest);
    SignInResponse getTokenByRefreshToken(String refreshToken) throws IllegalAccessException;

    UsuarioResponse getUserInfo();
    boolean validateToken(String token);
}
