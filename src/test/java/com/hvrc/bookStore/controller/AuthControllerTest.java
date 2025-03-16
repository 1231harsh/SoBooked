package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.jwt.JwtUtil;
import com.hvrc.bookStore.security.MyUserdetailService;
import com.hvrc.bookStore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private MyUserdetailService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignupSuccess() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userService.save(user)).thenReturn(user);

        ResponseEntity<String> response = authController.signup(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());

        verify(passwordEncoder, times(1)).encode("password");
        verify(userService, times(1)).save(user);
    }


    @Test
    void testSignupFailure() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        when(passwordEncoder.encode(user.getPassword())).thenThrow(new RuntimeException("Error occurred"));

        ResponseEntity<String> response = authController.signup(user);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error occurred", response.getBody());

        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void testLoginSuccess() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "testUser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtUtil.generateToken("testUser")).thenReturn("fake-jwt-token");

        ResponseEntity<?> response = authController.login(user);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("fake-jwt-token", responseBody.get("jwt"));
        assertEquals("ROLE_USER", responseBody.get("role"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername("testUser");
        verify(jwtUtil, times(1)).generateToken("testUser");
    }

    @Test
    void testLoginFailure_InvalidCredentials() {
        User user = new User();
        user.setUsername("wrongUser");
        user.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        ResponseEntity<?> response = authController.login(user);

        assertEquals(401, response.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Invalid username or password", responseBody.get("error"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLoginFailure_InternalServerError() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = authController.login(user);

        assertEquals(500, response.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("An unexpected error occurred", responseBody.get("error"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
