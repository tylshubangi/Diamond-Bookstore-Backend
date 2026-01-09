package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.CartResponseDTO;
import com.bookstore.onlinebookstore.service.CartService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ➕ Add to cart
    @PostMapping("/add")
    public CartResponseDTO addToCart(
            @RequestParam int bookId,
            @RequestParam int quantity) {
        return cartService.addToCart(bookId, quantity);
    }

    // 🔄 Update quantity (auto-remove at 0)
    @PutMapping("/update")
    public CartResponseDTO updateQuantity(
            @RequestParam int itemId,
            @RequestParam int quantity) {
        return cartService.updateQuantity(itemId, quantity);
    }

    // ❌ Remove item
    @DeleteMapping("/remove")
    public CartResponseDTO removeItem(@RequestParam int itemId) {
        return cartService.removeItem(itemId);
    }

    // 👀 View cart
    @GetMapping
    public CartResponseDTO viewCart() {
        return cartService.viewCart();
    }
}
