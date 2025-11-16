package com.hcltech.apigatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Configuration
public class JwtAuthManagerConfig {

    private final JwtUtil jwtUtil;

    public JwtAuthManagerConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> {
            String token = (String) authentication.getCredentials();
            if (token == null) {
                return Mono.empty();
            }

            String username = jwtUtil.extractUsername(token);
            if (username == null || !jwtUtil.validateToken(token, username)) {
                return Mono.empty();
            }

            var roles = jwtUtil.extractRoles(token);
            var authorities = roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            Authentication auth = new UsernamePasswordAuthenticationToken(username, token, authorities);
            return Mono.just(auth);
        };
    }
}