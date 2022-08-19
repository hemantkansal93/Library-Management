package com.library.management.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.library.management.model.Book;
import com.library.management.model.Reservation;
import com.library.management.service.BookService;
import com.library.management.service.ReservationService;

@Controller

public class AdminController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ReservationService reservationService;
	
	static String message= "";

	// Logger for logging user actions and many other processes happening into log file
		private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
		
	// Showing books when admin login and clicks Books Tab
	@GetMapping("/admin/books")
	public String getBooks(Model model) {
		LOGGER.info("Showing a list of all the books .....");
		List<Book> books = bookService.getAllBooks();
		model.addAttribute("books", books);
		model.addAttribute("message", message);
		model.addAttribute("username", HomeController.username);
		if(HomeController.username.equals(""))
			return "redirect:/";
		else
			return "admin/books/adminBooks";
	}
	
	// Showing users tab when admin login and clicks Users Tab
	@GetMapping("/admin/users")
	public String getUsers(Model model) {
		LOGGER.info("Showing users .....");
		model.addAttribute("username", HomeController.username);
		if(HomeController.username.equals(""))
			return "redirect:/";
		else
			return "admin/users/adminUsers";
	}
	

	// Showing reservations when admin login and clicks Reservations Tab
	@GetMapping("/admin/reservations")
	public String getAllReservations(Model model) {
		LOGGER.info("Showing reservations.....");
		List<Reservation> reservations = reservationService.getPendingReservations();
		model.addAttribute("reservations", reservations);
		model.addAttribute("username", HomeController.username);
		if(HomeController.username.equals(""))
			return "redirect:/";
		else
			return "admin/reservations/adminReservations";
	}
}
