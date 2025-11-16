package com.hcltech.universityservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@FeignClient(name = "AUTHSERVICE")
public interface AuthClient {
	@PostMapping("/api/auth/register")
    ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequestDTO registerRequest);
}
