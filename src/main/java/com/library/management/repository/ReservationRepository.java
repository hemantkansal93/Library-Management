package com.library.management.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.library.management.model.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

}
