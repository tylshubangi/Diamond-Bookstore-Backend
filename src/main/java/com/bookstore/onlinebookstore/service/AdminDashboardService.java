package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.repository.BookRepository;
import com.bookstore.onlinebookstore.repository.OrderRepository;
import com.bookstore.onlinebookstore.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    public AdminDashboardService(
            UserRepository userRepository,
            OrderRepository orderRepository,
            BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
    }

    public long totalUsers() {
        return userRepository.count();
    }

    public long totalOrders() {
        return orderRepository.count();
    }

    public BigDecimal totalSales() {
        return orderRepository.findAll()
                .stream()
                .map(order -> order.getTotalAmount())
                .filter(Objects::nonNull) // ✅ KEY FIX
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Book> lowStockBooks() {
        return bookRepository.findAll()
                .stream()
                .filter(book -> book.getStockQuantity() < 5)
                .toList();
    }
}
