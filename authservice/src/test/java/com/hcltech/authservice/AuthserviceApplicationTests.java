package com.hcltech.authservice;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;


@SpringBootTest
class AuthserviceApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		// Verifies Spring context loads without issues
		assertNotNull(context, "ApplicationContext should be initialized");
	}

	@Test
	void mainMethodRunsSuccessfully() {
		assertDoesNotThrow(() -> AuthserviceApplication.main(new String[] {}));
	}
}