package com.library.management.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.library.management.model.Book;
import com.library.management.model.User;
import com.library.management.service.BookService;
import com.library.management.service.LoginValidation;
import com.library.management.service.UserService;
import com.library.management.service.UserValidation;

@Controller
public class HomeController {

	@Autowired
	private UserValidation userValidation;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LoginValidation loginValidation;
	
	@Autowired
	private BookService bookService;
	
	private String validity ="";
	
	private String loginValidity = "";
	
	static String username = "";
	
	static String errorMessage = "";
	
	// Logger for logging user actions and many other processes happening into log file
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	// Login page
	@GetMapping("/")
	public String homepage(Model model) {
		LOGGER.info("Login........");
		User loginUser = new User();
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("errorMessage", loginValidity);
		return "home/homepage";
	}
	
	// Registration Page
	@GetMapping("/register")
	public String register(Model model) {
		LOGGER.info("User Registration........");
		User registerUser = new User();
		model.addAttribute("registerUser", registerUser);
		model.addAttribute("errorMessage", validity);
		loginValidity ="";
		return "home/registration";
	}

	// Verifying registration details
	@PostMapping("/verifyRegistration")
	public String verifyRegistration(@ModelAttribute("registeredUser") User user, Model model) {
		LOGGER.info("Verifying Registration........");
		validity = userValidation.validateUser(user);
		model.addAttribute("username", user.getUsername());
		model.addAttribute("password", user.getPassword());
		UserController.relatedBooks.clear();
		if(validity.equals("success")) {
			validity ="";
			user.setUserType("customer");
			userService.addUser(user);
			return "home/confirmRegistration";
		}
		else {
			return "redirect:/register";
		}
	}
	
	// Verifying login credentials
	@PostMapping("/home")
	public String loginToHomepage(@ModelAttribute("loginUser") User user, Model model) {
		LOGGER.info("Authenticating login credentials........");
		loginValidity = loginValidation.validateUser(user);
		username = user.getUsername();
		if(loginValidity.equals("success")) {
			return "redirect:/home";
		}
		return "redirect:/";
	}
	
	// After successful login, homepage for admin and customer
	@GetMapping("/home")
	public String redirectToHomePage(@ModelAttribute("loginUser") User user, Model model) {
		LOGGER.info("Homepage after successful login........");
		loginValidity = "";
		String userType = loginValidation.getUserType(user);
		model.addAttribute("username", username);
		if(userType.equals("admin")) {
			return "home/homepageAdmin";
		}
		else {
			List<Book> books = bookService.getAllBooks();
			model.addAttribute("books", books);
			return "home/homepageUser";
		}
	}
	
	// Logout
	@GetMapping("/logout")
	public String logout(Model model) {
		LOGGER.info("Log out done........");
		username="";
		errorMessage ="";
		return "redirect:/";
	}
}
