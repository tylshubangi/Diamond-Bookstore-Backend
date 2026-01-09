package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.OrderResponseDTO;
import com.bookstore.onlinebookstore.entity.OrderStatus;
import com.bookstore.onlinebookstore.service.OrderService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 👑 ADMIN: view all orders
    @GetMapping
    public List<OrderResponseDTO> allOrders() {
        return orderService.getAllOrders();
    }

    // 👑 ADMIN: update order status
    @PutMapping("/{orderId}/status")
    public OrderResponseDTO updateStatus(
            @PathVariable int orderId,
            @RequestParam OrderStatus status) {

        return orderService.updateOrderStatus(orderId, status);
    }
}
