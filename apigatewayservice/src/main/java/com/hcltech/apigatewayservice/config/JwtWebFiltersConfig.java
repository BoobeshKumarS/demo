package com.hcltech.apigatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class JwtWebFiltersConfig {

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(ReactiveAuthenticationManager authManager) {
        AuthenticationWebFilter authWebFilter = new AuthenticationWebFilter(authManager);

        // Stateless (no sessions)
        authWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        // Convert Authorization: Bearer <token> to Authentication
        authWebFilter.setServerAuthenticationConverter(this::convertFromBearer);

        // Run on any exchange — Spring will decide if it's needed based on matchers/permitAll
        authWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.anyExchange());

        return authWebFilter;
    }

    private Mono<org.springframework.security.core.Authentication> convertFromBearer(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }
        String token = authHeader.substring(7);
        // Put the token into credentials; ReactiveAuthenticationManager will validate it
        return Mono.just(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(null, token));
    }
}
