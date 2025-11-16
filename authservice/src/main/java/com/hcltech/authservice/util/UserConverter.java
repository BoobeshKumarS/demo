package com.hcltech.authservice.util;

import org.springframework.stereotype.Component;

import com.hcltech.authservice.dto.UserLoginResponseDTO;
import com.hcltech.authservice.dto.UserRegisterResponseDTO;
import com.hcltech.authservice.entity.User;

@Component
public class UserConverter {
	public UserRegisterResponseDTO registerEntityToResponse(User user) {
		UserRegisterResponseDTO registerResponse = new UserRegisterResponseDTO();
		registerResponse.setId(user.getId());
		registerResponse.setUsername(user.getUsername());
		registerResponse.setEmail(user.getEmail());
		registerResponse.setRoles(user.getRoles());
		return registerResponse;
	}
	
	public UserLoginResponseDTO loginEntityToResponse(User user) {
		UserLoginResponseDTO loginResponse = new UserLoginResponseDTO();
		loginResponse.setId(user.getId());
		loginResponse.setUsername(user.getUsername());
		loginResponse.setEmail(user.getEmail());
		loginResponse.setRoles(user.getRoles());
		return loginResponse;
	}
}
