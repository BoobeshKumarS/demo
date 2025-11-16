package com.hcltech.apigatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            AuthenticationWebFilter authenticationWebFilter
    ) {
        http
            // Stateless + no CSRF for APIs
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

            // Return clean 401/403 without Basic popup
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((exchange, e) -> {
                    var response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().remove(HttpHeaders.WWW_AUTHENTICATE);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    var body = """
                        {"error":"unauthorized","message":"Authentication required"}
                        """.getBytes(StandardCharsets.UTF_8);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(body)));
                })
                .accessDeniedHandler((exchange, e) -> {
                    var response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    response.getHeaders().remove(HttpHeaders.WWW_AUTHENTICATE);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    var body = """
                        {"error":"forbidden","message":"Insufficient permissions"}
                        """.getBytes(StandardCharsets.UTF_8);
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(body)));
                })
            )

            .authorizeExchange(exchange -> exchange
                // CORS preflight
                .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Swagger & Actuator
                .pathMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/webjars/**",
                    "/swagger-resources/**",
                    "/actuator/**",

                    // service-specific OpenAPI if you route them through gateway
                    "/authservice/v3/api-docs/**",
                    "/studentservice/v3/api-docs/**",
                    "/universityservice/v3/api-docs/**",
                    "/applicationservice/v3/api-docs/**"
                ).permitAll()

                // Auth endpoints (open)
                .pathMatchers(
                    "/api/auth/test",
                    "/api/auth/register",
                    "/api/auth/login"
                ).permitAll()

                // Registration
                .pathMatchers(
                    "/api/students/register",
                    "/api/admins/register"
                ).permitAll()

                // Public READ endpoints only
                .pathMatchers(HttpMethod.GET,
                    "/api/universities",
                    "/api/universities/*",
                    "/api/universities/*/courses",
                    "/api/courses",
                    "/api/courses/university/*",
                    "/api/courses/*"
                ).permitAll()

                // Role-protected
                .pathMatchers("/api/auth/**").hasAnyRole("ADMIN", "STUDENT")
                .pathMatchers("/api/students/**").hasAnyRole("STUDENT", "ADMIN")
                .pathMatchers(
                    "/api/admins/**",
                    "/api/universities/**",
                    "/api/courses/**"
                ).hasRole("ADMIN")

                // Everything else requires authentication
                .anyExchange().authenticated()
            )

            // Register your JWT AuthenticationWebFilter in the chain
            .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}