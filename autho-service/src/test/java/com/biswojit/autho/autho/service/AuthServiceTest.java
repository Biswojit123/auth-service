package com.biswojit.autho.autho.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.biswojit.autho.autho.dto.AuthResponse;
import com.biswojit.autho.autho.dto.EmailRequest;
import com.biswojit.autho.autho.dto.RegisterRequest;
import com.biswojit.autho.autho.entity.User;
import com.biswojit.autho.autho.exchandler.EmailAlreadyExistException;
import com.biswojit.autho.autho.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testRegisterUser_successfulRegistration() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Biswojit", "biswojit@example.com", "password123");

        when(userRepository.existsByEmail("biswojit@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(restTemplate.postForObject(
            anyString(),
            any(EmailRequest.class),
            eq(String.class))
        ).thenReturn("Email sent successfully.");

        // Act
        AuthResponse response = authService.registerUser(request);

        // Assert
        assertEquals("User registered successfully and Email sent successfully.", response.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_emailAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Biswojit", "biswojit@example.com", "password123");
        when(userRepository.existsByEmail("biswojit@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistException.class, () -> authService.registerUser(request));
    }

    @Test
    void testRegisterUser_emailSendingFails() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Biswojit", "biswojit@example.com", "password123");

        when(userRepository.existsByEmail("biswojit@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(restTemplate.postForObject(anyString(), any(EmailRequest.class), eq(String.class)))
            .thenThrow(new RuntimeException("API failure"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.registerUser(request));
    }
}
