package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.*;
import com.bookstore.onlinebookstore.entity.*;
import com.bookstore.onlinebookstore.repository.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final BookRepository bookRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    public WishlistService(
            WishlistRepository wishlistRepository,
            WishlistItemRepository wishlistItemRepository,
            BookRepository bookRepository,
            CartService cartService,
            UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.bookRepository = bookRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    // 🔐 Current user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ❤️ Get or create wishlist
    private Wishlist getOrCreateWishlist(User user) {
        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setUser(user);
                    return wishlistRepository.save(wishlist);
                });
    }

    // ➕ Add book to wishlist
    public WishlistResponseDTO addToWishlist(int bookId) {

        User user = getCurrentUser();
        Wishlist wishlist = getOrCreateWishlist(user);

        wishlistItemRepository
                .findByWishlist_IdAndBook_Id(wishlist.getId(), bookId)
                .ifPresent(i -> {
                    throw new RuntimeException("Book already in wishlist");
                });

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        WishlistItem item = new WishlistItem();
        item.setWishlist(wishlist);
        item.setBook(book);

        wishlistItemRepository.save(item);
        wishlist.getItems().add(item);

        return mapToDTO(wishlist);
    }

    // ❌ Remove from wishlist
    public WishlistResponseDTO removeFromWishlist(int itemId) {

        WishlistItem item = wishlistItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        Wishlist wishlist = item.getWishlist();
        wishlist.getItems().remove(item);

        wishlistItemRepository.delete(item);

        return mapToDTO(wishlist);
    }

    // 👀 View wishlist
    public WishlistResponseDTO viewWishlist() {
        User user = getCurrentUser();
        Wishlist wishlist = getOrCreateWishlist(user);
        return mapToDTO(wishlist);
    }

    // 🛒 Add wishlist item to cart
    public WishlistResponseDTO moveToCart(int itemId, int quantity) {

        WishlistItem item = wishlistItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        cartService.addToCart(item.getBook().getId(), quantity);

        Wishlist wishlist = item.getWishlist();
        wishlist.getItems().remove(item);
        wishlistItemRepository.delete(item);

        return mapToDTO(wishlist);
    }

    // 🔁 Mapper
    private WishlistResponseDTO mapToDTO(Wishlist wishlist) {

        WishlistResponseDTO dto = new WishlistResponseDTO();
        dto.setWishlistId(wishlist.getId());

        List<WishlistItemDTO> items = wishlist.getItems().stream().map(item -> {
            WishlistItemDTO d = new WishlistItemDTO();
            d.setItemId(item.getId());
            d.setBookId(item.getBook().getId());
            d.setTitle(item.getBook().getTitle());
            d.setPrice(item.getBook().getPrice());
            d.setImageUrl(item.getBook().getImageUrl());
            return d;
        }).toList();

        dto.setItems(items);
        return dto;
    }
}
