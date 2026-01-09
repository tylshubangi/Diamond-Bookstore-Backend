package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.OrderResponseDTO;
import com.bookstore.onlinebookstore.service.OrderService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 📦 PLACE ORDER
    @PostMapping
    public OrderResponseDTO placeOrder(@RequestParam String address) {
        return orderService.placeOrder(address);
    }

    // 👤 USER: view my orders
    @GetMapping
    public List<OrderResponseDTO> myOrders() {
        return orderService.getMyOrders();
    }

    // 👤 USER: view single order
    @GetMapping("/{orderId}")
    public OrderResponseDTO getOrder(@PathVariable int orderId) {
        return orderService.getOrderForUser(orderId);
    }

    // ❌ NO updateStatus HERE
}
