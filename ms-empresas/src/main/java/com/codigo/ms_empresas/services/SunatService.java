package com.codigo.ms_empresas.services;

import com.codigo.ms_empresas.aggregates.response.SunatResponse;
import com.codigo.ms_empresas.entity.EmpresaEntity;

public interface SunatService {
    SunatResponse buscarPorRUC(String numRuc);
    SunatResponse registrarEmpresa(String numRuc, String user);

    SunatResponse buscar(String numRuc);
}
