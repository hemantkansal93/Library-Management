package com.library.management.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.library.management.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
