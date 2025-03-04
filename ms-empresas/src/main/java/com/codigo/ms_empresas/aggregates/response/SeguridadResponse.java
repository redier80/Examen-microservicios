package com.codigo.ms_empresas.aggregates.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeguridadResponse {

    private Long id;
    private String nombres;
    private String apellidos;
    private String email;
    private String username;
    private String tipoDoc;
    private String numDoc;
}
