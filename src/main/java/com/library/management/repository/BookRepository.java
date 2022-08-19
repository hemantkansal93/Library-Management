package com.library.management.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.library.management.model.Book;


@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {

}
