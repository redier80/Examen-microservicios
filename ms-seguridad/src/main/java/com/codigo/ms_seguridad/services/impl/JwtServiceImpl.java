package com.codigo.ms_seguridad.services.impl;

import com.codigo.ms_seguridad.aggregates.constants.Constants;
import com.codigo.ms_seguridad.services.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
public class JwtServiceImpl implements JwtService {

    @Value("${key.signature}")
    private String keySignature;
    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 120000))
                .claim("type", Constants.ACCESS)
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
       final String username = extractUserName(token);
       return (username.equals(userDetails.getUsername())
                    && !isTokenExpired(token));


    }

    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1200000))
                .claim("type", Constants.REFRESH)
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        String tokenType = claims.get("type",String.class);
        return Constants.REFRESH.equalsIgnoreCase(tokenType);
    }

    @Override
    public boolean validateToken2(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSignKey())
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | SignatureException e) {
            return false;
        }
    }

    // Metodo que genera una llave para firmar los tokens
    private Key getSignKey(){
        byte [] key = Decoders.BASE64.decode(keySignature);
        return Keys.hmacShaKeyFor(key);
    }

    //METODO PARA EXTRAER EL PAYLOAD(CLAIMS) DEL TOKEN
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build()
                .parseClaimsJws(token).getBody();
    }

    //METODO PARA OBTENER UN ELEMENTO (ATRIBUTO) DEL PAYLOAD (CLAIMS)
    private <T> T extractClaim(String token,
                               Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(extractAllClaims(token));
    }

    private boolean isTokenExpired(String token){
      return extractClaim(token, Claims::getExpiration).before(new Date());
    }


}
