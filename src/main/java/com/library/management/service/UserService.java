package com.library.management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.management.model.User;
import com.library.management.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReservationService reservationService;
	
	//	Getting user by id
	public User getUserById(String id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElseGet(null);
	}
	
	//	Getting all the admin users
	public List<User> getAllAdminUsers() {
		List<User> users = new ArrayList<>();
		List<User> adminUsers = new ArrayList<>();
		users = (List<User>) userRepository.findAll();
		for(User user:users) {
			if(user.getUserType().equals("admin")) {
				adminUsers.add(user);
			}
		}
		return adminUsers;
	}
	
	//	Getting all the customer users
	public List<User> getAllCustomerUsers() {
		List<User> users = new ArrayList<>();
		List<User> customerUsers = new ArrayList<>();
		users = (List<User>) userRepository.findAll();
		for(User user:users) {
			if(user.getUserType().equals("customer")) {
				customerUsers.add(user);
			}
		}
		return customerUsers;
	}
	
	//	Deleting a user by id
	public String deleteUser(String username) {
		User user = getUserById(username);
		if(!reservationService.isCustomerActive(username)){
			userRepository.deleteById(username);
		}
		return user.getUserType();
	}
	
	// 	Adding a user into database
	public void addUser(User user) {
		userRepository.save(user);
	}
	
	// Updating a user
	public void updateUserById(User updatedUser) {
		if(!reservationService.isCustomerActive(updatedUser.getUsername())){
			userRepository.save(updatedUser);
		}
	}
}
