package com.library.management.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

//To define that this entity (class) is going to persist in database
@Entity

//For setter and getter methods
@Data
public class Reservation {
	// Primary key
	@Id
	
	// Auto generated values
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int transactionId;
	
	private String username;
	private int resourceId;
	
	private String borrowDate;
	private String returnDate;
	
	private double fine;
	private String whoReserved;
	
	@Transient
	private String isbn;
	
	@Transient
	private String title;
	
	@Transient
	private String category;
	
	@Transient
	private String author;
	
	@Transient
	private String publisher;
	
	@Transient
	private int	pageCount;
	
	@Transient
	private String formattedBorrowDate;
	
	@Transient
	private String	formattedReturnDate;
}
