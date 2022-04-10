package com.acrud.acrudplus;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "ACRUD API",
				description = "API Definitions for ACRUD Microservice",
				version = "3.0.1"
		))
public class AcrudPlusApplication {
	public static void main(String[] args) {
		SpringApplication.run(AcrudPlusApplication.class, args);
	}
}
