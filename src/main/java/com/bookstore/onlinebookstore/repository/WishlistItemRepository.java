package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Integer> {

    Optional<WishlistItem> findByWishlist_IdAndBook_Id(int wishlistId, int bookId);
}
