package com.epsi.middle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.epsi.*"})
@ConfigurationPropertiesScan("com.epsi.config")
public class MiddleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiddleApplication.class, args);
	}
	
}
