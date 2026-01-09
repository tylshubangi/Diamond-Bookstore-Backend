package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Cart;
import com.bookstore.onlinebookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser(User user);
}
