package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.WishlistResponseDTO;
import com.bookstore.onlinebookstore.service.WishlistService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // ➕ Add book to wishlist
    @PostMapping("/add/{bookId}")
    public WishlistResponseDTO add(@PathVariable int bookId) {
        return wishlistService.addToWishlist(bookId);
    }

    // ❌ Remove book from wishlist
    @DeleteMapping("/remove/{itemId}")
    public WishlistResponseDTO remove(@PathVariable int itemId) {
        return wishlistService.removeFromWishlist(itemId);
    }

    // 👀 View wishlist
    @GetMapping
    public WishlistResponseDTO view() {
        return wishlistService.viewWishlist();
    }

    // 🛒 Move wishlist item to cart
    @PostMapping("/move-to-cart/{itemId}")
    public WishlistResponseDTO moveToCart(
            @PathVariable int itemId,
            @RequestParam(defaultValue = "1") int quantity) {
        return wishlistService.moveToCart(itemId, quantity);
    }
}
