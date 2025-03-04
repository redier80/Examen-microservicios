package com.codigo.ms_empresas.client;

import com.codigo.ms_empresas.aggregates.response.SeguridadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "MS-SEGURIDAD")
public interface ClienteSeguridad {

    @PostMapping("/apis/codigo/auth/validate-token")
    Boolean validateToken(@RequestHeader("Authorization") String token);

    @GetMapping("/apis/codigo/auth/user-info")
    SeguridadResponse userInfo(@RequestHeader("Authorization") String token);

}

