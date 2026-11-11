package com.angel.conversion_unidades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ConversionUnidadesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConversionUnidadesApplication.class, args);
	}

}
