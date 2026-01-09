package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.*;
import com.bookstore.onlinebookstore.entity.*;
import com.bookstore.onlinebookstore.repository.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            BookRepository bookRepository,
            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
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

    // 📦 PLACE ORDER
    public OrderResponseDTO placeOrder(String address) {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryAddress(address);
        order.setItems(new ArrayList<>());

        order = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            Book book = cartItem.getBook();

            if (book.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for " + book.getTitle());
            }

            // reduce stock
            book.setStockQuantity(
                    book.getStockQuantity() - cartItem.getQuantity());
            bookRepository.save(book);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(book.getPrice());

            order.getItems().add(orderItem);

            totalAmount = totalAmount.add(
                    book.getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setTotalAmount(totalAmount);

        orderItemRepository.saveAll(order.getItems());
        orderRepository.save(order);

        // clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return mapToDTO(order);
    }

    // ❌ CANCEL ORDER
    public OrderResponseDTO cancelOrder(int orderId) {

        User user = getCurrentUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ✅ FIX: int comparison (NO .equals)
        if (order.getUser().getId() != user.getId()) {
            throw new RuntimeException("Unauthorized");
        }

        if (order.getStatus() == OrderStatus.SHIPPED) {
            throw new RuntimeException("Order already shipped");
        }

        for (OrderItem item : order.getItems()) {
            Book book = item.getBook();
            book.setStockQuantity(
                    book.getStockQuantity() + item.getQuantity());
            bookRepository.save(book);
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return mapToDTO(order);
    }

    // 👤 USER: My Orders
    public List<OrderResponseDTO> getMyOrders() {
        User user = getCurrentUser();

        return orderRepository.findByUser(user)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // 👤 USER: Single Order
    public OrderResponseDTO getOrderForUser(int orderId) {

        User user = getCurrentUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ✅ FIX: int comparison (NO .equals)
        if (order.getUser().getId() != user.getId()) {
            throw new RuntimeException("Unauthorized");
        }

        return mapToDTO(order);
    }

    // 👑 ADMIN: All Orders
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // 👑 ADMIN: Update Status
    public OrderResponseDTO updateOrderStatus(int orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        return mapToDTO(order);
    }

    // 🔁 DTO MAPPER (READ-ONLY)
    private OrderResponseDTO mapToDTO(Order order) {

        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setOrderId(order.getId());
        dto.setStatus(order.getStatus().name());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponseDTO> items = new ArrayList<>();

        for (OrderItem item : order.getItems()) {

            OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
            itemDTO.setBookId(item.getBook().getId());
            itemDTO.setTitle(item.getBook().getTitle());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setQuantity(item.getQuantity());

            items.add(itemDTO);
        }

        dto.setItems(items);

        return dto;
    }
}
