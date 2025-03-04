package com.codigo.ms_seguridad.aggregates.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UsuarioResponse {
    private Long id;
    private String nombres;
    private String apellidos;
    private String email;
    private String username;
    private String tipoDoc;
    private String numDoc;
}
