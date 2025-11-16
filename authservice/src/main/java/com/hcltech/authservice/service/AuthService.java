package com.hcltech.authservice.service;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.hcltech.authservice.dto.MessageResponseDTO;
import com.hcltech.authservice.dto.UserLoginRequestDTO;
import com.hcltech.authservice.dto.UserLoginResponseDTO;
import com.hcltech.authservice.dto.UserRegisterRequestDTO;
import com.hcltech.authservice.dto.UserRegisterResponseDTO;

public interface AuthService {

	UserLoginResponseDTO login(UserLoginRequestDTO loginRequest);

	ResponseEntity<MessageResponseDTO> register(UserRegisterRequestDTO registerRequest);

    ResponseCookie logoutUser();

    UserRegisterResponseDTO getCurrentUserDetails(Authentication authentication);
}
