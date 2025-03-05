package com.codigo.ms_empresas.controller;

import com.codigo.ms_empresas.aggregates.response.SeguridadResponse;
import com.codigo.ms_empresas.aggregates.response.SunatResponse;
import com.codigo.ms_empresas.client.SeguridadClient;
import com.codigo.ms_empresas.services.SunatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas/v1")
@RequiredArgsConstructor
public class ComplementarioController {

    private final SeguridadClient seguridadClient;
    private final SunatService sunatService;

    @PostMapping("/registrar/{numRuc}")
    public ResponseEntity<?> registrarEmpresa(@RequestHeader("Authorization") String token,
                                              @PathVariable String numRuc){
        if (seguridadClient.validateToken(token)){

            SeguridadResponse seguridadResponse= seguridadClient.getInfoUser(token);

            var user= seguridadResponse.getUsername();
            SunatResponse empresaEntity = sunatService.registrarEmpresa(numRuc,user);
            return ResponseEntity.ok(empresaEntity);
        }else {
            return ResponseEntity.status(401).body("Token invalido");
        }
    }

    @GetMapping("/buscar/{numRuc}")
    public ResponseEntity<?> buscar(@RequestHeader("Authorization") String token,
                                                @PathVariable String numRuc){
        if (seguridadClient.validateToken(token)) {
            return new ResponseEntity<>(sunatService.buscar(numRuc), HttpStatus.OK);
        }else {
            return ResponseEntity.status(401).body("Token invalido");
        }
    }
    @GetMapping("/test/{numRuc}")
    public ResponseEntity<?> test(@PathVariable String numRuc){
        return ResponseEntity.ok(sunatService.buscarPorRUC(numRuc));
    }

}
