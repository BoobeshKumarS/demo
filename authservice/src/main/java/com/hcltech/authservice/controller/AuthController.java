package com.hcltech.authservice.controller;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.hcltech.authservice.dto.MessageResponseDTO;
import com.hcltech.authservice.dto.UserLoginRequestDTO;
import com.hcltech.authservice.dto.UserLoginResponseDTO;
import com.hcltech.authservice.dto.UserRegisterRequestDTO;
import com.hcltech.authservice.dto.UserRegisterResponseDTO;
import com.hcltech.authservice.service.AuthService;

//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
    private AuthService authService;

    // ✅ LOGIN → Generates JWT, stores in HttpOnly cookie
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> authenticateUser(@Valid @RequestBody UserLoginRequestDTO loginRequest) {
    	logger.debug("Login Request Received ---> {}", loginRequest);
        UserLoginResponseDTO result = authService.login(loginRequest);

        // result contains: ResponseCookie + UserInfoResponse
        return ResponseEntity.ok().body(result);
    }

    // ✅ SIGNUP → Registers new user
    @PostMapping("/register")
    public ResponseEntity<MessageResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO registerRequest) {
    	logger.debug("Signup Request ---> {}", registerRequest);
        return authService.register(registerRequest);
    }

    // ✅ CURRENT USERNAME
    @GetMapping("/username")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public String currentUserName(Authentication authentication) {
        if (authentication != null) {
        	logger.debug("Authenticated username ---> {}", authentication.getName());
            return authentication.getName();
        } else {
            return "";
        }
    }

    // ✅ CURRENT USER DETAILS (with roles, email, etc.)
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<UserRegisterResponseDTO> getUserDetails(Authentication authentication) {
    	logger.debug("Authenticated user details ---> {}", authentication);
        return ResponseEntity.ok(authService.getCurrentUserDetails(authentication));
    }

    // ✅ LOGOUT → Clears the JWT cookie
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDTO> signoutUser() {
        ResponseCookie cookie = authService.logoutUser();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponseDTO("You've been signed out!"));
    }
}
