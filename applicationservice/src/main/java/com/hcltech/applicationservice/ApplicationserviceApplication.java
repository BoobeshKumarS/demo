package com.hcltech.applicationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApplicationserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationserviceApplication.class, args);
	}

}
