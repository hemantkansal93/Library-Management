package com.library.management.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.management.model.Book;
import com.library.management.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private ReservationService reservationService;
	
	// Getting all the books
	public List<Book> getAllBooks() {
		List<Book> books = new ArrayList<>();
		books = (List<Book>) bookRepository.findAll();
		return books;
	}
	
	// Getting book by id
	public Book getBookById(int id) {
		Optional<Book> student = bookRepository.findById(id);
		return student.orElseGet(null);
	}
	
	// Adding a book into database
	public void addBook(Book book) {
		bookRepository.save(book);
		
	}
	
	//	Deleting a book by id (if not issued to any user)
	public String deleteBook(int id) {
		if(!reservationService.isBookReserved(id)){
			bookRepository.deleteById(id);
			return "Book is deleted successfully";
		}
		return "Book can not be removed beacuse it is issued to the user.";
		
	}

	//	Updating a book by id (if not issued to any user)
	public String updateBookById(Book updatedBook) {
		if(!reservationService.isBookReserved(updatedBook.getResourceId())){
			bookRepository.save(updatedBook);
			return "Book is updated successfully";
		}
		return "Book can not be updated beacuse it is issued to the user.";
	}

	// Find books as searched by user
	public List<Book> findRelatedBooks(Book book) {
		HashSet<Book> relatedBooks = new HashSet<>();
		List<Book> books = getAllBooks();
		for(Book b:books) {
			if(b.getIsbn().equals(book.getIsbn())) {
				relatedBooks.add(b);
			}
			if(b.getAuthor().contains(book.getAuthor()) && !book.getAuthor().equals("")) {
				relatedBooks.add(b);
			}
			if(b.getCategory().contains(book.getCategory()) && !book.getCategory().equals("")) {
				relatedBooks.add(b);
			}
			if(b.getTitle().contains(book.getTitle()) && !book.getTitle().equals("")) {
				relatedBooks.add(b);
			}
			if(b.getPublisher().contains(book.getPublisher()) && !book.getPublisher().equals("")) {
				relatedBooks.add(b);
			}
		}
		List<Book> relatedAllBooks = (ArrayList<Book>)relatedBooks.stream().collect(Collectors.toList());
		return relatedAllBooks;
	}
}
