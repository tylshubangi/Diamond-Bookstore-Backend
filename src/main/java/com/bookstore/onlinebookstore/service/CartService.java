package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.CartItemResponseDTO;
import com.bookstore.onlinebookstore.dto.CartResponseDTO;
import com.bookstore.onlinebookstore.entity.*;
import com.bookstore.onlinebookstore.repository.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            BookRepository bookRepository,
            UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // 🔐 Get logged-in user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 🛒 Get or create cart
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setItems(new ArrayList<>());
                    return cartRepository.save(cart);
                });
    }

    // ➕ Add to cart
    public CartResponseDTO addToCart(int bookId, int quantity) {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        for (CartItem item : cart.getItems()) {
            if (item.getBook().getId() == bookId) {
                item.setQuantity(item.getQuantity() + quantity);
                cartItemRepository.save(item);
                return mapToCartDTO(cart);
            }
        }

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setBook(book);
        item.setQuantity(quantity);

        cart.getItems().add(item);
        cartItemRepository.save(item);

        return mapToCartDTO(cart);
    }

    // 🔄 Update quantity (AUTO-REMOVE if quantity == 0)
    public CartResponseDTO updateQuantity(int itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            return removeItem(itemId);
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return mapToCartDTO(item.getCart());
    }

    // ❌ Remove item
    public CartResponseDTO removeItem(int itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Cart cart = item.getCart();
        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        return mapToCartDTO(cart);
    }

    // 👀 View cart
    public CartResponseDTO viewCart() {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        return mapToCartDTO(cart);
    }

    // 💰 Calculate total
    private BigDecimal calculateTotal(Cart cart) {
        return cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ✅ DTO MAPPER (THIS IS WHAT YOU WERE MISSING)
    private CartResponseDTO mapToCartDTO(Cart cart) {

        CartResponseDTO response = new CartResponseDTO();

        // 🧼 If cart is empty → no cartId
        if (cart.getItems().isEmpty()) {
            response.setItems(List.of());
            response.setGrandTotal(BigDecimal.ZERO);
            return response;
        }

        response.setCartId(cart.getId());

        List<CartItemResponseDTO> items = cart.getItems().stream().map(item -> {
            CartItemResponseDTO dto = new CartItemResponseDTO();
            dto.setItemId(item.getId());
            dto.setBookId(item.getBook().getId());
            dto.setTitle(item.getBook().getTitle());
            dto.setPrice(item.getBook().getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setTotalPrice(item.getTotalPrice());
            return dto;
        }).toList();

        response.setItems(items);
        response.setGrandTotal(calculateTotal(cart));

        return response;
    }
}
