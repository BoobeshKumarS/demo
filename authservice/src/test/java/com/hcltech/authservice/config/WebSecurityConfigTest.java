package com.hcltech.authservice.config;

import com.hcltech.authservice.entity.User;
import com.hcltech.authservice.entity.UserRole;
import com.hcltech.authservice.repository.UserRepository;
import com.hcltech.authservice.security.JwtAuthenticationFilter;
import com.hcltech.authservice.security.MyUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebSecurityConfigTest {

    @InjectMocks
    private WebSecurityConfig webSecurityConfig;

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private UserRepository userRepository;
    
	
	@Mock
	private HttpServletRequest request;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticationProviderBean() {
        when(passwordEncoder.encode("test")).thenReturn("encodedTest");

        AuthenticationProvider provider = webSecurityConfig.authenticationProvider(userDetailsService, passwordEncoder);

        assertNotNull(provider);
        assertTrue(provider.supports(org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAuthenticationManagerBean() throws Exception {
        AuthenticationManager mockManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockManager);

        AuthenticationManager manager = webSecurityConfig.authenticationManager(authenticationConfiguration);

        assertNotNull(manager);
        assertEquals(mockManager, manager);
    }

//    @Test
//    void testCorsConfigurationSourceBean() {
//        var corsSource = webSecurityConfig.corsConfigurationSource();
//        when(request.getRequestURI()).thenReturn("/"); // Mock request URI
//        var corsConfig = corsSource.getCorsConfiguration(request);
//
//        assertNotNull(corsConfig);
//        assertTrue(corsConfig.getAllowedOrigins().contains("http://localhost:3000"));
//        assertTrue(corsConfig.getAllowedMethods().contains("GET"));
//        assertTrue(corsConfig.getAllowCredentials());
//    }

//    @Test
//    void testSecurityFilterChainBean() throws Exception {
//        var http = org.springframework.security.config.annotation.web.builders.HttpSecurity.class;
//        // We cannot fully build HttpSecurity without Spring context, but we can check bean creation
//        SecurityFilterChain chain = webSecurityConfig.securityFilterChain(
//                mock(org.springframework.security.config.annotation.web.builders.HttpSecurity.class),
//                userDetailsService,
//                passwordEncoder,
//                jwtAuthenticationFilter
//        );
//        assertNotNull(chain);
//    }

    @Test
    void testInitDefaultAdmin_createsAdminIfNotExists() throws Exception {
        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(passwordEncoder.encode("adminPass")).thenReturn("encodedPass");

        var runner = webSecurityConfig.initDefaultAdmin(userRepository, passwordEncoder);
        runner.run();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("admin", savedUser.getUsername());
        assertEquals("admin@gmail.com", savedUser.getEmail());
        assertEquals("encodedPass", savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(UserRole.ADMIN));
    }

    @Test
    void testInitDefaultAdmin_skipsIfAdminExists() throws Exception {
        when(userRepository.existsByUsername("admin")).thenReturn(true);

        var runner = webSecurityConfig.initDefaultAdmin(userRepository, passwordEncoder);
        runner.run();

        verify(userRepository, never()).save(any(User.class));
    }
}