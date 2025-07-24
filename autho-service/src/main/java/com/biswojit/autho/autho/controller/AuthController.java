package com.biswojit.autho.autho.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biswojit.autho.autho.dto.AuthResponse;
import com.biswojit.autho.autho.dto.LoginRequest;
import com.biswojit.autho.autho.dto.LoginResponse;
import com.biswojit.autho.autho.dto.RegisterRequest;
import com.biswojit.autho.autho.dto.UserProfileResponse;
import com.biswojit.autho.autho.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService service;
	
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		AuthResponse reponse = service.registerUser(request);
		return new ResponseEntity<AuthResponse>(reponse,HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		LoginResponse response = service.login(request);
		return new ResponseEntity<LoginResponse>(response,HttpStatus.OK);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
		System.out.println("==== PROFILE API CALLED ====");
		// Extracted from JWT by Spring Security
		String email = authentication.getName();
		
		UserProfileResponse response = service.getProfile(email);
		
		return new ResponseEntity<UserProfileResponse>(response,HttpStatus.OK);
		
	}
}
