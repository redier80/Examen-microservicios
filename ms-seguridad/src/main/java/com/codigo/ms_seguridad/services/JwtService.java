package com.codigo.ms_seguridad.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String extractUserName(String token);
    String generateToken( UserDetails userDetails);
    boolean validateToken(String token, UserDetails userDetails);
    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
    boolean isRefreshToken(String token);
    boolean validateToken2(String token);
}
