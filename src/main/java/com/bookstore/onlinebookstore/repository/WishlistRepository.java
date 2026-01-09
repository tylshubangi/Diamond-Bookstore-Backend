package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    Optional<Wishlist> findByUser(User user);
}
