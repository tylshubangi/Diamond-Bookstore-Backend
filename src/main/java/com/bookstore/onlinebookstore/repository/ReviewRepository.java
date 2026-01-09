package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByBook(Book book);

    Optional<Review> findByUserAndBook(User user, Book book);

    List<Review> findAll();

}
