package com.hcltech.authservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI customOpenAPI() {
    	String bearerAuth = "BearerAuth";
    	
    	Server apiGatewayService = new Server()
                .url("http://localhost:8080")
                .description("API-Gateway");

        Server authService = new Server()
                .url("http://localhost:8081")
                .description("Auth-Service");
        
		return new OpenAPI()
				.info(new Info().title("Auth Service API").version("1.0")
						.description("API documentation for Auth Service"))
				.servers(List.of(apiGatewayService, authService))
				.addSecurityItem(new SecurityRequirement().addList(bearerAuth))
				.components(new io.swagger.v3.oas.models.Components().addSecuritySchemes(bearerAuth,
						new SecurityScheme().name(bearerAuth).type(SecurityScheme.Type.HTTP).scheme("bearer")
								.bearerFormat("JWT")));
	}
} 