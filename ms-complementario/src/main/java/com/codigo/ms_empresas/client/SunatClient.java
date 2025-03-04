package com.codigo.ms_empresas.client;

import com.codigo.ms_empresas.aggregates.response.SunatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client-sunat",url="https://api.apis.net.pe/v2/sunat/")
public interface SunatClient {

    @GetMapping(value = "/ruc", produces = "application/json" )
    SunatResponse getEmpresa(@RequestParam("numero") String numero,
                             @RequestHeader("Authorization") String authorization);

}
