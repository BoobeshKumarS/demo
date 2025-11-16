package com.hcltech.authservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hcltech.authservice.dto.MessageResponseDTO;
import com.hcltech.authservice.dto.UserLoginRequestDTO;
import com.hcltech.authservice.dto.UserLoginResponseDTO;
import com.hcltech.authservice.dto.UserRegisterRequestDTO;
import com.hcltech.authservice.dto.UserRegisterResponseDTO;
import com.hcltech.authservice.entity.User;
import com.hcltech.authservice.entity.UserRole;
import com.hcltech.authservice.exception.UserNotFoundException;
import com.hcltech.authservice.repository.UserRepository;
import com.hcltech.authservice.security.MyUserDetailsService;
import com.hcltech.authservice.service.AuthService;
import com.hcltech.authservice.util.JwtUtil;
import com.hcltech.authservice.util.UserConverter;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserConverter userConverter;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public UserLoginResponseDTO login(UserLoginRequestDTO loginRequest) {
	    User userToLogin = userRepository.findByEmail(loginRequest.getEmail())
	            .orElseThrow(() -> new UserNotFoundException("User not found", "Email", loginRequest.getEmail()));

	    // ✅ Use raw password from request, not encoded one
	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    UserDetails userDetails = userDetailsService.loadUserByUsername(userToLogin.getEmail());
		String token = jwtUtil.generateToken(userDetails.getUsername(), userToLogin.getEmail(), userToLogin.getRoles());
		long validityHours = jwtUtil.getTokenValidityInHours(token);

	    UserLoginResponseDTO response = userConverter.loginEntityToResponse(userToLogin);
	    response.setToken(token);
	    response.setExpiry(validityHours);

	    return response;
	}

	@Override
	public ResponseEntity<MessageResponseDTO> register(UserRegisterRequestDTO registerRequest) {
		if (userRepository.existsByUsername(registerRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Username already taken!"));
		}
		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Email already in use!"));
		}

		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(encoder.encode(registerRequest.getPassword()));
		
		Set<UserRole> roles = new HashSet<>();
		if (registerRequest.getRoles()==null) {
			roles.add(UserRole.STUDENT);
			user.setRoles(roles);
		} else {
			if (registerRequest.getRoles().contains(UserRole.STUDENT)) {
				roles.add(UserRole.STUDENT);
			}
			if (registerRequest.getRoles().contains(UserRole.ADMIN)) {
				roles.add(UserRole.ADMIN);
			}
			user.setRoles(roles);
		}
		
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponseDTO("User registered successfully!"));
	}

	@Override
	public ResponseCookie logoutUser() {
		return ResponseCookie.from("jwt", null).path("/").httpOnly(true).maxAge(0).build();
	}

	@Override
	public UserRegisterResponseDTO getCurrentUserDetails(Authentication authentication) {
		if (authentication == null)
			return null;
		String identifier = authentication.getName();
		
		 // ✅ Try email first, then username
	    User user = userRepository.findByEmail(identifier)
	            .or(() -> userRepository.findByUsername(identifier))
	            .orElseThrow(() -> new RuntimeException("User not found: " + identifier));

		return userConverter.registerEntityToResponse(user);
	}
}
