package com.library.management.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.management.model.Reservation;
import com.library.management.repository.ReservationRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	//	Getting all the reservations
	public List<Reservation> getAllReservations() {
		List<Reservation> reservations = new ArrayList<>();
		reservations = (List<Reservation>) reservationRepository.findAll();
		return reservations;
	}

	// Getting all pending reservations (where book has not been returned)
	public List<Reservation> getPendingReservations() {
		List<Reservation> reservations = new ArrayList<>();
		List<Reservation> allReservations = (List<Reservation>) reservationRepository.findAll();
		for (Reservation reservation : allReservations) {
			if (reservation.getReturnDate() == null) {
				reservations.add(reservation);
			}
		}
		return reservations;
	}

	// Getting reservation by id
	public Reservation getReservationById(int id) {
		Optional<Reservation> reservation = reservationRepository.findById(id);
		return reservation.orElseGet(null);
	}

	// Reserving a book
	public String reserveBook(Reservation reservation) {
		int bookId = reservation.getResourceId();
		int booksAlreadyReserved = 0;
		List<Reservation> reservations = new ArrayList<>();
		reservations = getAllReservations();
		boolean valid = true;
		String message = "";
		for (Reservation reserve : reservations) {
			if (reserve.getUsername().equals(reservation.getUsername()) && reserve.getReturnDate() == null) {
				booksAlreadyReserved++;
			}
			if (reserve.getResourceId() == (bookId) && reserve.getReturnDate() == null)
				valid = false;
		}
		if (!valid) {
			message = "Book is already borrowed to some other student";
		}

		if (booksAlreadyReserved == 4) {
			message = "User has already reached his/her maximum limit of reserving the books";
		}
		if (valid && booksAlreadyReserved < 4) {
			reservationRepository.save(reservation);
			message = "success";
		}
		return message;
	}

	// Updating reservation
	public String updateReservation(Reservation reservation) {
		int booksAlreadyReserved = 0;
		List<Reservation> reservations = new ArrayList<>();
		reservations = getAllReservations();
		String message = "";
		for (Reservation reserve : reservations) {
			if (reserve.getUsername().equals(reservation.getUsername()) && reserve.getReturnDate() == null) {
				booksAlreadyReserved++;
			}
		}
		if (booksAlreadyReserved == 4) {
			message = "User has already reached his/her maximum limit of reserving the books";
		} else {
			reservationRepository.save(reservation);
			message = "success";
		}
		return message;
	}

	// Deleting reservation
	public void deleteReservation(int transactionId) {
		reservationRepository.deleteById(transactionId);
	}

	// CHeck if book is already reserved
	public boolean isBookReserved(int id) {
		List<Reservation> reservations = getAllReservations();
		for (Reservation reservation : reservations) {
			if (reservation.getResourceId() == id && reservation.getReturnDate() == null)
				return true;
		}
		return false;
	}

	// Getting pending reservations by user
	public List<Reservation> getReservationsByUsername(String username) {
		List<Reservation> reservations = new ArrayList<>();
		List<Reservation> allReservations = (List<Reservation>) reservationRepository.findAll();
		for (Reservation reservation : allReservations) {
			if (reservation.getUsername().equals(username) && (reservation.getReturnDate() == null)) {
				reservations.add(reservation);
			}
		}
		return reservations;
	}

	// Returning a book
	public void returnBook(int book_id) {
		List<Reservation> pendingReservations = getPendingReservations();
		for (Reservation reservation : pendingReservations) {
			if (reservation.getResourceId() == book_id) {
				reservation.setReturnDate(new Date().toString());
				reservationRepository.save(reservation);
			}
		}
	}

	// Getting all reservations by user
	public List<Reservation> getAllReservationsByUsername(String username) {
		List<Reservation> allReservations = new ArrayList<>();
		List<Reservation> reservations = (List<Reservation>) reservationRepository.findAll();
		;
		for (Reservation reservation : reservations) {
			if (reservation.getUsername().equals(username)) {
				allReservations.add(reservation);
			}
		}
		return allReservations;
	}

	// Changing date format for 'users' UI (dd-MM-yyyy)
	public String changeDateFormat(String dateString) {
		String pattern = "dd-MM-yyyy";
		long longDate = Date.parse(dateString);
		Date date = new Date(longDate);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String formattedDate = simpleDateFormat.format(date);
		return formattedDate;
	}

	// Check if user has some books due
	public boolean isCustomerActive(String username) {
		List<Reservation> reservations = getAllReservations();
		for (Reservation reservation : reservations) {
			if (reservation.getUsername().equals(username) && reservation.getReturnDate() == null)
				return true;
		}
		return false;
	}
}