package com.codigo.ms_empresas.services.Impl;

import com.codigo.ms_empresas.aggregates.constants.Constants;
import com.codigo.ms_empresas.aggregates.response.SunatResponse;
//import com.codigo.ms_empresas.client.ClienteSunat;
import com.codigo.ms_empresas.entity.EmpresaEntity;
import com.codigo.ms_empresas.repository.EmpresaRepository;
import com.codigo.ms_empresas.services.SunatService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service

public class SunatServiceImpl implements SunatService {
    //@Autowired
    //private  ClienteSunat clienteSunat;
    @Autowired
    private  EmpresaRepository empresaRepository;
    @Value("${token.api}")
    private String token;
    @Override
    public SunatResponse buscarPorRUC(String numRuc) {
        return execution(numRuc);
    }

    @Override
    public SunatResponse registrarEmpresa(String numRuc, String user) {

        // Consumir API externa
        SunatResponse consultaSunat = execution(numRuc);

        // Si no se encontraron datos en SUNAT, lanzar excepción
        if (Objects.isNull(consultaSunat)) {
            throw new RuntimeException("No se encontraron datos para el RUC: " + numRuc);
        }

        // Construir entidad con Builder
        EmpresaEntity empresaEntity= new  EmpresaEntity();

        empresaEntity.setRazonSocial(consultaSunat.getRazonSocial());
        empresaEntity.setTipoDocumento(consultaSunat.getTipoDocumento());
        empresaEntity.setNumeroDocumento(consultaSunat.getNumeroDocumento());
        empresaEntity.setEstado(consultaSunat.getEstado());
        empresaEntity.setCondicion(consultaSunat.getCondicion());
        empresaEntity.setDireccion(consultaSunat.getDireccion());
        empresaEntity.setUbigeo(consultaSunat.getUbigeo());
        empresaEntity.setViaTipo(consultaSunat.getViaTipo());
        empresaEntity.setViaNombre(consultaSunat.getViaNombre());
        empresaEntity.setZonaCodigo(consultaSunat.getZonaCodigo());
        empresaEntity.setZonaTipo(consultaSunat.getZonaTipo());
        empresaEntity.setNumero(consultaSunat.getNumero());
        empresaEntity.setInterior(consultaSunat.getInterior());
        empresaEntity.setLote(consultaSunat.getLote());
        empresaEntity.setDpto(consultaSunat.getDpto());
        empresaEntity.setManzana(consultaSunat.getManzana());
        empresaEntity.setKilometro(consultaSunat.getKilometro());
        empresaEntity.setDistrito(consultaSunat.getDistrito());
        empresaEntity.setProvincia(consultaSunat.getProvincia());
        empresaEntity.setDepartamento(consultaSunat.getDepartamento());
        empresaEntity.setEsAgenteRetencion(consultaSunat.getEsAgenteRetencion());
        empresaEntity.setEsBuenContribuyente(consultaSunat.getEsBuenContribuyente());
        empresaEntity.setLocalesAnexos(consultaSunat.getLocalesAnexos());
        empresaEntity.setTipo(consultaSunat.getTipo());
        empresaEntity.setActividadEconomica(consultaSunat.getActividadEconomica());
        empresaEntity.setNumeroTrabajadores(consultaSunat.getNumeroTrabajadores());
        empresaEntity.setTipoFacturacion(consultaSunat.getTipoFacturacion());
        empresaEntity.setTipoContabilidad(consultaSunat.getTipoContabilidad());
        empresaEntity.setComercioExterior(consultaSunat.getComercioExterior());
        empresaEntity.setUsuarioRegistro(user);
        empresaEntity.setFechaRegistro(new Timestamp(System.currentTimeMillis()));


        // Guardar en la base de datos
        empresaRepository.save(empresaEntity);

        // Retornar la respuesta de SUNAT
        return consultaSunat;
    }

    @Override
    public SunatResponse buscar(String numRuc) {
        Optional<EmpresaEntity> empresaOpt = empresaRepository.findByNumeroDocumento(numRuc);

        if (empresaOpt.isPresent()) {
            EmpresaEntity empresa = empresaOpt.get();

            // Convertir la entidad a SunatResponse
            return SunatResponse.builder()
                    .razonSocial(empresa.getRazonSocial())
                    .tipoDocumento(empresa.getTipoDocumento())
                    .numeroDocumento(empresa.getNumeroDocumento())
                    .estado(empresa.getEstado())
                    .condicion(empresa.getCondicion())
                    .direccion(empresa.getDireccion())
                    .ubigeo(empresa.getUbigeo())
                    .viaTipo(empresa.getViaTipo())
                    .viaNombre(empresa.getViaNombre())
                    .zonaCodigo(empresa.getZonaCodigo())
                    .zonaTipo(empresa.getZonaTipo())
                    .numero(empresa.getNumero())
                    .interior(empresa.getInterior())
                    .lote(empresa.getLote())
                    .dpto(empresa.getDpto())
                    .manzana(empresa.getManzana())
                    .kilometro(empresa.getKilometro())
                    .distrito(empresa.getDistrito())
                    .provincia(empresa.getProvincia())
                    .departamento(empresa.getDepartamento())
                    .EsAgenteRetencion(empresa.getEsAgenteRetencion())
                    .EsBuenContribuyente(empresa.getEsBuenContribuyente())
                    .localesAnexos(empresa.getLocalesAnexos())
                    .tipo(empresa.getTipo())
                    .actividadEconomica(empresa.getActividadEconomica())
                    .numeroTrabajadores(empresa.getNumeroTrabajadores())
                    .tipoFacturacion(empresa.getTipoFacturacion())
                    .tipoContabilidad(empresa.getTipoContabilidad())
                    .comercioExterior(empresa.getComercioExterior())
                    .build();
        } else {
            throw new RuntimeException("Empresa con RUC " + numRuc + " no encontrada.");
        }

    }

    //EJECUTANDO EL API EXTERNA
    private SunatResponse execution(String numRuc){
        String tokenOk = "Bearer "+token;
        //return clienteSunat.getEmpresa(numRuc,tokenOk);
        return null;
    }
}
