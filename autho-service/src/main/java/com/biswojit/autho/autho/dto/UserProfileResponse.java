package com.biswojit.autho.autho.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
	private String name;
	private String email;
	private String role;
}
