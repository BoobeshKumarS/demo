package com.hcltech.authservice.util;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hcltech.authservice.entity.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.expiration}")
	private long jwtExpirationInMs;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public long getTokenValidityInHours(String token) {
		Date expiration = extractClaim(token, Claims::getExpiration);
		long durationMillis = expiration.getTime() - System.currentTimeMillis();
		return TimeUnit.MILLISECONDS.toHours(durationMillis);
	}

	public String extractRole(String token) {
		return extractClaim(token, claims -> claims.get("role", String.class));
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	public boolean validateToken(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(username) && !isTokenExpired(token));
	}

	private Boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	public String generateToken(String username, String email, Set<UserRole> roles) {
		Map<String, Object> claims = new HashMap<>();
//	       claims.put("roles", roles); // Ensure role is correctly added
		claims.put("roles", roles.stream().map(r -> "ROLE_" + r.name()).toList());

		return createToken(claims, username, email);
	}

	private Key getSigningKey() {
		byte[] keyBytes = Base64.getDecoder().decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private String createToken(Map<String, Object> claims, String subject, String email) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(email)
				.claim("email", email)
				.claim("username", subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
				.signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
	}
	
	public List<String> extractRoles(String token) {
	    return extractClaim(token, claims -> claims.get("roles", List.class));
	}
}