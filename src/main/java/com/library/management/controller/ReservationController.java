package com.library.management.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.management.model.Book;
import com.library.management.model.Reservation;
import com.library.management.model.User;
import com.library.management.service.BookService;
import com.library.management.service.ReservationService;
import com.library.management.service.UserValidation;

@Controller

public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private UserValidation userValidation;

	@Autowired
	private BookService bookService;

	private static String usernameRequested = "";
	private static List<Book> relatedBooks = new ArrayList<>();
	
	// Logger for logging user actions and many other processes happening into log file
		private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

	// Showing all the transactions happened so far
	@GetMapping("/admin/allTransactions")
	public String getAllReservations(Model model) {
		LOGGER.info("Showing a list of all the transactions(if any).....");
		HomeController.errorMessage = "";
		List<Reservation> reservations = reservationService.getAllReservations();
		model.addAttribute("reservations", reservations);
		model.addAttribute("username", HomeController.username);
		if(HomeController.username.equals(""))
			return "redirect:/";
		else
			return "admin/reservations/allTransactions";
	}
	
	// Check if User exists before reserving a book
	@GetMapping("/admin/reserve/checkUser")
	public String getReserveBookForm(Model model) {
		LOGGER.info("Admin is trying to reserve a book for the user. Step.1: Checking if user exists.....");
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("username", HomeController.username);
		model.addAttribute("errorMessage", HomeController.errorMessage);
		if (HomeController.username.equals(""))
			return "redirect:/";
		else
			return "admin/reservations/reserveBookFormCheckUser";
	}

	// Verifying user before reserving a book
	@PostMapping("/admin/reserve/checkUser")
	public String checkUsername(@ModelAttribute("user") User user, Model model) {
		boolean isUsernameRegistered = userValidation.doesUsernameExists(user.getUsername());
		if (isUsernameRegistered) {
			LOGGER.info("Admin is trying to reserve a book for the user. Step.2: User exists");
			HomeController.errorMessage = "";
			usernameRequested = user.getUsername();
			relatedBooks.clear();
			return "redirect:/admin/reserve/checkBook";
		}
		else {
			LOGGER.info("Error message: User doesn't exists.....");
			HomeController.errorMessage = "Username is not registered !!!";
			return "redirect:/admin/reserve/checkUser";
		}
	}

	// Book which needs to be reserved
	@GetMapping("/admin/reserve/checkBook")
	public String checkBookForm(Model model) {
		LOGGER.info("Admin is trying to reserve a book for the user. Step.3: Now checking if book is available?....");
		model.addAttribute("username", HomeController.username);
		model.addAttribute("errorMessage", HomeController.errorMessage);
		model.addAttribute("books", relatedBooks);
		model.addAttribute("book", new Book());
		if (HomeController.username.equals(""))
			return "redirect:/";
		else {
			HomeController.errorMessage = "";
			return "admin/reservations/reserveBookFormCheckBookAvailabilty";
		}
	}

	// Checking if book is available before committing a transaction
	@PostMapping("/admin/reserve/getBooks")
	public String getBooks(@ModelAttribute("book") Book book, Model model) {
		relatedBooks.clear();
		List<Book> books = bookService.findRelatedBooks(book);
		if (books.size() == 0) {
			LOGGER.info("Admin is trying to reserve a book for the user. Step.4: No book is available as per the search ....");
			HomeController.errorMessage = "No books found related to your search";
		} else {
			LOGGER.info("Admin is trying to reserve a book for the user. Step.4: Getting a list of available books ....");
			relatedBooks = books;
		}

		if (HomeController.username.equals(""))
			return "redirect:/";
		else
			return "redirect:/admin/reserve/checkBook";
	}

	//Reserving a book
	@GetMapping("/admin/reserve/book")
	public String reserveBook(@RequestParam("bookId") String bookId) {
		Reservation reservation = new Reservation();
		reservation.setUsername(usernameRequested);
		reservation.setResourceId(Integer.parseInt(bookId));
		reservation.setBorrowDate(new Date().toString());
		reservation.setWhoReserved(HomeController.username);
		String message = reservationService.reserveBook(reservation);
		if (message.equals("success")) {
			LOGGER.info("Admin is trying to reserve a book for the user. Step.5: Book is reserved.....");
			return "redirect:/admin/reservations";
		} else {
			LOGGER.info("Admin is trying to reserve a book for the user. Step.5: Error: Book can not be reserved beacuse" + message + ".....");
			HomeController.errorMessage = message;
			if (message.contains("maximum limit"))
				return "redirect:/admin/reserve/checkUser";
			else if (message.contains("already borrowed"))
				return "redirect:/admin/reserve/checkBook";
		}
		return "redirect:/admin/reservations/adminReservations";
	}

	//	Form for modifying reservations
	@GetMapping("/admin/reservations/modifyReservation")
	public String modifyReservation(@RequestParam String transactionId, Model model) {
		Reservation reservation = reservationService.getReservationById(Integer.parseInt(transactionId));
		model.addAttribute("username", HomeController.username);
		model.addAttribute("reservation", reservation);
		return "admin/reservations/updateReservationForm";
	}

	// Updating reservations
	@PostMapping("/admin/reservations/updateReservation")
	public String updateReservation(@ModelAttribute("reservation") Reservation reservation, String bookId) {
		String message = reservationService.updateReservation(reservation);
		if (message.equals("success")) {
			return "redirect:/admin/reservations";
		} else {
			HomeController.errorMessage = message;
			if (message.contains("maximum limit"))
				return "redirect:/admin/reserve/checkUser";
		}
		return "redirect:/admin/reservations/adminReservations";
	}

	// Delete Reservation
	@GetMapping("/admin/reservations/deleteReservation")
	public String deleteReservation(@RequestParam String transactionId, Model model) {
		LOGGER.info("Reservation is deleted.....");
		reservationService.deleteReservation(Integer.parseInt(transactionId));
		return "redirect:/admin/reservations";
	}

	// Check user before returning a book
	@GetMapping("/admin/return/checkUser")
	public String getReturnBookForm(Model model) {
		LOGGER.info("Admin is trying to return a book for the user. Step.1: Checking if user exists.....");
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("username", HomeController.username);
		model.addAttribute("errorMessage", HomeController.errorMessage);
		if (HomeController.username.equals(""))
			return "redirect:/";
		else
			return "admin/reservations/returnBookFormCheckUser";
	}

	// Verifying user before returning a book
	@PostMapping("/admin/return/checkUser")
	public String verifyUsername(@ModelAttribute("user") User user, Model model) {
		boolean isUsernameRegistered = userValidation.doesUsernameExists(user.getUsername());
		if (isUsernameRegistered) {
			LOGGER.info("Admin is trying to return a book for the user. Step.2: User exists.....");
			usernameRequested =  user.getUsername();
			return "redirect:/admin/return/reservedBooks";

		} else {
			LOGGER.info("Admin is trying to return a book for the user. Step.2: User doesn't exists.....");
			HomeController.errorMessage = "Username is not registered !!!";
			return "redirect:/admin/return/checkUser";
		}
	}

	// Showing a list of reserved books so that user can select which book he/she wants to return
	@GetMapping("admin/return/reservedBooks")
	public String getReservedBooks(Model model) {
		List<Book> reservedBooks = new ArrayList<>();
		List<Reservation> reservations = reservationService.getReservationsByUsername(usernameRequested);

		if (reservations.size() == 0) {
			LOGGER.info("Admin is trying to return a book for the user. Step.3: There is no book which was reserved by the user.....");
			HomeController.errorMessage = "There is no pending book left for the user: " + usernameRequested;
			return "redirect:/admin/return/checkUser";
		} else {
			LOGGER.info("Admin is trying to return a book for the user. Step.3: showing list of books which are due .....");
			for (Reservation reservation : reservations) {
				Book book = bookService.getBookById(reservation.getResourceId());
				reservedBooks.add(book);
			}
			model.addAttribute("books", reservedBooks);
			model.addAttribute("username", HomeController.username);
			return "admin/reservations/reservedBooksByUsername";
		}
	}

	//	 Returning a book
	@GetMapping("admin/return/book")
	public String returnBook(@RequestParam("bookId") String bookId) {
		LOGGER.info("Admin is trying to return a book for the user. Step.3: Book is returned.....");
		reservationService.returnBook(Integer.parseInt(bookId));
		return "redirect:/admin/return/reservedBooks";
	}

}
