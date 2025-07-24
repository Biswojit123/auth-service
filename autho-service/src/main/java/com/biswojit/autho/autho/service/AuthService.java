package com.biswojit.autho.autho.service;

import com.biswojit.autho.autho.dto.AuthResponse;
import com.biswojit.autho.autho.dto.LoginRequest;
import com.biswojit.autho.autho.dto.LoginResponse;
import com.biswojit.autho.autho.dto.RegisterRequest;
import com.biswojit.autho.autho.dto.UserProfileResponse;

public interface AuthService {
	public AuthResponse registerUser(RegisterRequest registerRequest);

	public LoginResponse login(LoginRequest request);

	public UserProfileResponse getProfile(String email);
}
