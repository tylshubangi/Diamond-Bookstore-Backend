package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
