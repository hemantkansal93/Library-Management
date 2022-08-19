package com.library.management.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.management.model.User;
import com.library.management.repository.UserRepository;

@Service
public class LoginValidation {

	@Autowired
	private UserRepository userRepository;

	private String userType = "";
	
	// Login Validation
	public String validateUser(User user) {
		String validity = "";
		
		String username = user.getUsername();
		String password = user.getPassword();
		
		if(username.equals("") || username == null) {
			validity = "Username is mandatory field. Please enter your correct username/email";
		}
		else if(doesUsernameExists(username)) {
			validity = "Username does not exist. Please register before login.";
		}
		else if(password.equals("") || password == null) {
			validity = "Password is mandatory field. Please enter your correct password";
		}
		else if(!isValid(username, password)) {
			validity = "Password is incorrect. Please enter your correct password";
		}
		else {
			validity = "success";
		}
		return validity;
	}

	// Check if username exists or not
	private boolean doesUsernameExists(String username) {
		Optional<User> user  = userRepository.findById(username);
		if(user != null && !user.isEmpty()) {
			return false;
		}
		return true;
	}
	
	// Check if login is successful
	private boolean isValid(String username, String password) {
		Optional<User> user  = userRepository.findById(username);
		if(user != null && !user.isEmpty()) {
			if(user.get().getPassword().equals(password)) {
				userType = user.get().getUserType();
				return true;
			}
		}
		return false;
	}
	
	// Returning userType
	public String getUserType(User user) {
		return userType;
	}
}
