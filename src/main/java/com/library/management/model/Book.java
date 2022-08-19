package com.library.management.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Data;

//To define that this entity (class) is going to persist in database
@Entity

//For setter and getter methods
@Data
public class Book {
	
	// Primary key
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int resourceId;
	private String isbn;
	private String title;
	private String category;
	private String author;
	private String publisher;
	private int	pageCount;
	
	
	// Defining relationship between 2 entities (Book and Reservation)
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	
	// Join column with other table
	@JoinColumn(name = "resourceId")
	
	private List<Reservation> reservations;
	
}
