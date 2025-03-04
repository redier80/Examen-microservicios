package com.codigo.ms_empresas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsEmpresasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsEmpresasApplication.class, args);
	}

}
