package com.codigo.ms_empresas.repository;

import com.codigo.ms_empresas.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Long > {

    Optional<EmpresaEntity> findByNumeroDocumento(String numRuc);
}
