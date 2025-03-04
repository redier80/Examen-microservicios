package com.codigo.ms_empresas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsComplementarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsComplementarioApplication.class, args);
	}

}
