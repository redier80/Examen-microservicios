package com.codigo.ms_empresas.controller;


import com.codigo.ms_empresas.aggregates.response.SeguridadResponse;
import com.codigo.ms_empresas.aggregates.response.SunatResponse;
import com.codigo.ms_empresas.client.ClienteSeguridad;
import com.codigo.ms_empresas.services.SunatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empresas/v1")
@RequiredArgsConstructor
public class EmpresaController {

    private final SunatService sunatService;
    private final ClienteSeguridad clienteSeguridad;

    @PostMapping("/registrar/{numRuc}")
    public ResponseEntity<?> registrarEmpresa(@RequestHeader("Authorization") String token,
                                        @PathVariable String numRuc){
        if (clienteSeguridad.validateToken(token)){

            SeguridadResponse seguridadResponse= clienteSeguridad.userInfo(token);
            var user= seguridadResponse.getUsername();
            SunatResponse empresaEntity = sunatService.registrarEmpresa(numRuc,user);
            return ResponseEntity.ok(empresaEntity);
        }else {
            return ResponseEntity.status(401).body("Token invalido");
        }


    }



    @GetMapping("/buscar/{numRuc}")
    public ResponseEntity<SunatResponse> buscar(@PathVariable String numRuc){
        return new ResponseEntity<>(sunatService.buscar(numRuc), HttpStatus.OK);
    }
}
