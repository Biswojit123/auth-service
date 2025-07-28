package com.biswojit.autho.autho.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.biswojit.autho.autho.config.JwtUtil;
import com.biswojit.autho.autho.dto.AuthResponse;
import com.biswojit.autho.autho.dto.EmailRequest;
import com.biswojit.autho.autho.dto.LoginRequest;
import com.biswojit.autho.autho.dto.LoginResponse;
import com.biswojit.autho.autho.dto.RegisterRequest;
import com.biswojit.autho.autho.dto.UserProfileResponse;
import com.biswojit.autho.autho.entity.User;
import com.biswojit.autho.autho.exchandler.EmailAlreadyExistException;
import com.biswojit.autho.autho.repo.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RestTemplate restTemplate;
	
	@Transactional
	@Override
	public AuthResponse registerUser(RegisterRequest registerRequest) {

		// Checking the User Exist the requested email id or not
		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			// That line call to the Global exception handler
			throw new EmailAlreadyExistException("Email Already Exist..");
		}

		// Builder to set the Value to user
		User user = User.builder().name(registerRequest.getName()).email(registerRequest.getEmail())
				.password(passwordEncoder.encode(registerRequest.getPassword())).role("USER").build();

		// prepare email
		EmailRequest message = EmailRequest
				.builder()
				.to(user.getEmail())
				.subject("!Welcome 🤞👍💕 to Filpcark")
				.body("Hi " + user.getName() + ",\n\nYour account has been created successfully!" + ",\n\n User Name:"
						+ user.getName() + " ,\n\n password:" + user.getPassword())
				.build();
		
		//response
		String response ;
		try {
			//call the Notification Service
			response = restTemplate.postForObject(
					"http://localhost:8082/notify/send-email", //url
					message,// message or email 
					String.class // response from api
					);
		}catch(Exception e) {
			throw new RuntimeException("Email sending failed. Registration aborted.");
		}
		
		// Save the Repository
		userRepository.save(user);

		// return the Response
		return new AuthResponse("User registered successfully and "+response);
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		//Check the Email is present or not 
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Invalid Email.."));
		
		//After checking the email check the password
		boolean match = passwordEncoder.matches(request.getPassword(), user.getPassword());
		
		//check the password
		if(!match) {
			throw new RuntimeException("Invalid Password.....");
		}
		
		//Generate the Token
		String token = JwtUtil.generateToken(user.getEmail());
		return new LoginResponse(token, "Login Successfully");
	}

	@Override
	public UserProfileResponse getProfile(String email) {
		
		User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    UserProfileResponse response = new UserProfileResponse(
	            user.getName(),
	            user.getEmail(),
	            user.getRole()
	    );
	    
		return response;
	}

}
