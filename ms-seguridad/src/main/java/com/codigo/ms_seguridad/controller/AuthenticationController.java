package com.codigo.ms_seguridad.controller;

import com.codigo.ms_seguridad.aggregates.request.SignInRequest;
import com.codigo.ms_seguridad.aggregates.request.SignUpRequest;
import com.codigo.ms_seguridad.aggregates.request.ValidateTokenRequest;
import com.codigo.ms_seguridad.aggregates.response.SignInResponse;
import com.codigo.ms_seguridad.aggregates.response.UsuarioResponse;
import com.codigo.ms_seguridad.entity.Usuario;
import com.codigo.ms_seguridad.services.AuthenticationService;
import com.codigo.ms_seguridad.services.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.security.Key;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("auth/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @Value("${key.signature}")
    private String secretKey;

    @PostMapping("/register/signupuser")
    public ResponseEntity<Usuario> signUpUser(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.signUpUser(signUpRequest));
    }

    @PostMapping("/register/signupadmin")
    public ResponseEntity<Usuario> signUpAdmin(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.signUpAdmin(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> sigIn(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }

/*    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody ValidateTokenRequest request) {
        try {
            String username = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(request.getToken())
                    .getBody()
                    .getSubject();

            // Comprobar si el token pertenece al usuario
            if (!username.equals(request.getUsername())) {
                return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Token no pertenece al usuario"));
            }

            return ResponseEntity.ok(Map.of("valid", true, "message", "Token válido"));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Token expirado"));
        } catch (SignatureException e) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Firma del token no válida"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Token inválido"));
        }
    }
*/
    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Remover "Bearer " del token
        }

        boolean isValid = authenticationService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }


    @GetMapping("/user-info")
    public ResponseEntity<UsuarioResponse> getUserInfo() {
        return ResponseEntity.ok(authenticationService.getUserInfo());
    }

    @GetMapping("/clave")
    public ResponseEntity<String> getClave(){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String dato = Base64.getEncoder().encodeToString(key.getEncoded());
        return ResponseEntity.ok(dato);
    }
}
