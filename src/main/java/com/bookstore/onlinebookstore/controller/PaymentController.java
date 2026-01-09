package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.*;
import com.bookstore.onlinebookstore.service.PaymentService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 💳 Pay for order
    @PostMapping("/pay")
    public PaymentResponseDTO pay(@RequestBody PaymentRequestDTO request) {
        return paymentService.makePayment(request);
    }
}
