package com.hcltech.authservice.security;

import com.hcltech.authservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private FilterChain filterChain;
    
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotFilter_whenPathStartsWithAuthenticate() {
        when(request.getRequestURI()).thenReturn("/authenticate/login");

        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        assert result;
    }

    @Test
    void shouldFilter_whenPathIsDifferent() {
        when(request.getRequestURI()).thenReturn("/api/data");

        boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

        assert !result;
    }

    @Test
    void doFilterInternal_noAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    void doFilterInternal_invalidAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic token");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    void doFilterInternal_validJwt_setsAuthentication() throws ServletException, IOException {
        String jwt = "valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtil.extractUsername(jwt)).thenReturn("testUser");
        when(jwtUtil.extractRoles(jwt)).thenReturn(List.of("ROLE_USER"));
        when(jwtUtil.validateToken(jwt, "testUser")).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() != null;
        assert SecurityContextHolder.getContext().getAuthentication().getName().equals("testUser");
    }

    @Test
    void doFilterInternal_invalidJwt_noAuthentication() throws ServletException, IOException {
        String jwt = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtil.extractUsername(jwt)).thenReturn("testUser");
        when(jwtUtil.extractRoles(jwt)).thenReturn(List.of("ROLE_USER"));
        when(jwtUtil.validateToken(jwt, "testUser")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }
}