package com.hcltech.authservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

	private SwaggerConfig swaggerConfig;

	@BeforeEach
	void setUp() {
		swaggerConfig = new SwaggerConfig();
	}

	@Test
	void testCustomOpenAPIConfiguration() {
		OpenAPI openAPI = swaggerConfig.customOpenAPI();

		// Validate OpenAPI object
		assertNotNull(openAPI);

		// Validate Info section
		Info info = openAPI.getInfo();

		assertNotNull(info);
		assertEquals("Auth Service API", info.getTitle());
		assertEquals("1.0", info.getVersion());
		assertTrue(info.getDescription().contains("API documentation"));

		// Validate Servers
		assertNotNull(openAPI.getServers());
		assertEquals(2, openAPI.getServers().size());
		assertTrue(
				openAPI.getServers().stream().map(Server::getUrl).anyMatch(url -> url.equals("http://localhost:8080")));
		assertTrue(
				openAPI.getServers().stream().map(Server::getUrl).anyMatch(url -> url.equals("http://localhost:8081")));

		// Validate Security Scheme
		var components = openAPI.getComponents();
		assertNotNull(components);
		SecurityScheme scheme = components.getSecuritySchemes().get("BearerAuth");
		assertNotNull(scheme);
		assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
		assertEquals("bearer", scheme.getScheme());
		assertEquals("JWT", scheme.getBearerFormat());
	}
}