package com.hcltech.authservice.dto;

import java.util.Set;
import java.util.UUID;

import com.hcltech.authservice.entity.UserRole;

import lombok.Data;

@Data
public class UserLoginResponseDTO {
	private UUID id;
	private String username;
	private String email;
	private Set<UserRole> roles;
	private String token;
	private Long expiry;
}
