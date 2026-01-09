package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.entity.Order;
import com.bookstore.onlinebookstore.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser(User user);

    // ✅ REQUIRED FOR REVIEWS MODULE
    boolean existsByUserAndItems_Book(User user, Book book);
}
