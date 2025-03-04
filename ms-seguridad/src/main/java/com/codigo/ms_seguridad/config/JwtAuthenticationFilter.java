package com.codigo.ms_seguridad.config;

import com.codigo.ms_seguridad.services.JwtService;
import com.codigo.ms_seguridad.services.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String tokenExtraidoHeader = request.getHeader("Authorization");
        final String tokenLimpio;
        final String userName;

        //Validar el encabezado de la solicitud, validar el token
        if(!StringUtils.hasText(tokenExtraidoHeader)
                || !StringUtils.startsWithIgnoreCase(tokenExtraidoHeader,"Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        //Limpiando nuestro token
        tokenLimpio = tokenExtraidoHeader.substring(7);
        userName = jwtService.extractUserName(tokenLimpio);

        //Validamos si el usuario no es nulo o vacio y que no hay alguna autenticación
        if(Objects.nonNull(userName)
                && SecurityContextHolder.getContext().getAuthentication() == null){
            //Definimos un contexto de seguridad vacio
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            //Recuperar los detalles del usuario desde base de datos
            UserDetails userDetails = usuarioService.userDetailsService().loadUserByUsername(userName);
            //validamos el token
            if(jwtService.validateToken(tokenLimpio,userDetails) &&
                    !jwtService.isRefreshToken(tokenLimpio)){
                //Objeto que representa al usuario autenticado
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                //Agregamos detalles adicionales sobre el objeto de autenticación
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //Establecer el contexto de seugridad creado anteriomente como vacio
                securityContext.setAuthentication(authenticationToken);
                //Asigname el contexto al holder de seguridad
                SecurityContextHolder.setContext(securityContext);
            }
        }
        //Continuar
        filterChain.doFilter(request,response);
    }
}
