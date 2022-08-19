package com.library.management.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.management.model.User;
import com.library.management.repository.UserRepository;

@Service
public class UserValidation {

	@Autowired
	private UserRepository userRepository;

	// Validating user details during registration
	public String validateUser(User user) {
		String validity = "";

		String name = user.getName();
		int age = user.getAge();
		String username = user.getUsername();
		String password = user.getPassword();
		String confirmPassword = user.getConfirmPassword();
		String profession = user.getProfession();
		String address = user.getAddress();
		String city = user.getCity();
		String state = user.getState();
		String country = user.getCountry();
		String mobile = user.getMobile();

		if (name.equals("") || name == null) {
			validity = "Name is mandatory field. Please enter your correct name";
		} else if (age < 1) {
			validity = "Age is mandatory field. Please enter your correct age";
		} else if (username.equals("") || username == null) {
			validity = "Username is mandatory field. Please enter your correct username/email";
		} else if (doesUsernameExists(username)) {
			validity = "Username already exists. Please enter unique username/email";
		} else if (password.equals("") || password == null) {
			validity = "Password is mandatory field. Please enter your correct password";
		} else if (confirmPassword.equals("") || confirmPassword == null) {
			validity = "Confirm Password is mandatory field. Please enter the same password as you entered in the previous field.";
		} else if (!confirmPassword.equals(password)) {
			validity = "Confirm Password and Password don't match. Please enter the same password as you entered in the previous field.";
		} else if (profession.equals("") || profession == null) {
			validity = "Profession is mandatory field. Please enter your profession";
		} else if (address.equals("") || address == null) {
			validity = "Address is mandatory field. Please enter your correct address";
		} else if (city.equals("") || city == null) {
			validity = "City is mandatory field. Please enter the correct city";
		} else if (state.equals("") || state == null) {
			validity = "State is mandatory field. Please enter the correct state";
		} else if (country.equals("") || country == null) {
			validity = "Country is mandatory field. Please enter the correct country";
		} else if (mobile.equals("")) {
			validity = "Mobile contact is mandatory field. Please enter the correct mobile number.";
		} else {
			validity = "success";
		}

		return validity;
	}

	// Check if username already exists
	public boolean doesUsernameExists(String username) {
		Optional<User> user = userRepository.findById(username);
		if (user != null && !user.isEmpty()) {
			return true;
		}
		return false;
	}

}
