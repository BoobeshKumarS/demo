package com.hcltech.authservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hcltech.authservice.entity.User;
import com.hcltech.authservice.entity.UserRole;
import com.hcltech.authservice.repository.UserRepository;
import com.hcltech.authservice.security.JwtAuthenticationFilter;
import com.hcltech.authservice.security.MyUserDetailsService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	private static final Logger securityLogger = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, MyUserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// CORS preflight
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

						.requestMatchers("/authservice/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
								"/actuator/**", "/error",

								"/api/auth/test",

								"/api/auth/register", "/api/auth/login")
						.permitAll().requestMatchers("/api/auth/**").hasAnyRole("ADMIN", "STUDENT").anyRequest()
						.authenticated())
				.authenticationProvider(authenticationProvider(userDetailsService, passwordEncoder))
//				.addFilterAfter(new JWTTokenGeneratorFilter(), org.springframework.security.web.authentication.www.BasicAuthenticationFilter.class)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // ✅ React app only
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public AuthenticationProvider authenticationProvider(MyUserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public CommandLineRunner initDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (!userRepository.existsByUsername("admin")) {
				User admin = new User();

				admin.setUsername("admin");
				admin.setEmail("admin@gmail.com");
				admin.setPassword(passwordEncoder.encode("adminPass"));

				// Assign ADMIN role
				Set<UserRole> roles = new HashSet<>();
				roles.add(UserRole.ADMIN);
				admin.setRoles(roles);

				userRepository.save(admin);
				securityLogger.info("Default ADMIN user created: admin / adminPass");
			} else {
				securityLogger.info("Admin user already exists. Skipping creation.");
			}
		};
	}

}
