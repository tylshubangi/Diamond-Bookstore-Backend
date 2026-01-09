package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.*;
import com.bookstore.onlinebookstore.entity.*;
import com.bookstore.onlinebookstore.repository.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository,
            OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public PaymentResponseDTO makePayment(PaymentRequestDTO request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ✅ Validate card details ONLY for card payments
        if (request.getPaymentMethod().equalsIgnoreCase("CARD")
                || request.getPaymentMethod().equalsIgnoreCase("DEBIT_CARD")
                || request.getPaymentMethod().equalsIgnoreCase("CREDIT_CARD")) {

            if (request.getCardNumber() == null
                    || request.getCardNumber().length() != 16) {
                throw new RuntimeException("Invalid card number");
            }
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(
                PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));

        payment.setStatus(PaymentStatus.SUCCESS); // mock success
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // ✅ Mark order as PAID / DELIVERED step
        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);

        return new PaymentResponseDTO(
                "Payment successful",
                payment.getStatus().name(),
                payment.getId());
    }
}
