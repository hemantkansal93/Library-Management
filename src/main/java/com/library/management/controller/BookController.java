package com.library.management.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.management.model.Book;
import com.library.management.service.BookService;

@Controller

public class BookController {

	@Autowired
	private BookService bookService;
	// Logger for logging user actions and many other processes happening into log file
		private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);
	
	// Showing form for adding a book
	@GetMapping("/admin/books/addBook")
	public String addBook(Model model) {
		LOGGER.info("Showing form for adding a book .....");
		AdminController.message = "";
		Book book = new Book();
		model.addAttribute("book", book);
		model.addAttribute("username", HomeController.username );
		if(HomeController.username.equals(""))
			return "redirect:/";
		else
			return "admin/books/addBookForm";
	}
	
	// Confirmation if books has been added successfully
	@PostMapping("/admin/books/confirmation")
	public String confirmAddBook(@ModelAttribute("book") Book book, Model model) {
		LOGGER.info("Showing form for adding a book .....");
		AdminController.message = "";
		bookService.addBook(book);
		model.addAttribute("username", HomeController.username);
		if(HomeController.username.equals(""))
			return "redirect:/";
		else
			return "admin/books/addBookConfirmation";
	}
	
	// Deleting a book only if it has not been issued to any user
	@GetMapping("admin/books/deleteBook")
	public String deleteBook(@RequestParam(value = "bookId", required = true) String bookId, Model model) {
		LOGGER.info("Deleting a book .....");
		AdminController.message = bookService.deleteBook(Integer.parseInt(bookId));
		model.addAttribute("username", HomeController.username);
		if(HomeController.username.equals(""))
			return "redirect:/";
		else
			return "redirect:/admin/books";
	}
	
	// Form for updating a book
	@GetMapping("admin/books/modifyBook")
	public String modifyBook(@RequestParam(value = "bookId", required = true) String bookId, Model model) {
		LOGGER.info("Showing form for modifying a book .....");
		AdminController.message = "";
		Book book = bookService.getBookById(Integer.parseInt(bookId));
		model.addAttribute("book", book);
		model.addAttribute("username", HomeController.username);
		if(HomeController.username.equals(""))
			return "redirect:/";
		else
			return "admin/books/updateBookForm";
	}
	
	// Updating a book
	@PostMapping("/admin/books/updateBook/{resourceId}")
	public String updateBook(@PathVariable("resourceId") String resourceId, @ModelAttribute("book") Book book, Model model) {
		LOGGER.info("Updating a book .....");
		book.setResourceId(Integer.parseInt(resourceId));
		AdminController.message = bookService.updateBookById(book);
		model.addAttribute("username", HomeController.username);
		if(HomeController.username.equals(""))
			return "redirect:/";
		else
			return "redirect:/admin/books";
	}
	
}
