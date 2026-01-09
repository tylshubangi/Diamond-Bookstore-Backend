package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Order;
import com.bookstore.onlinebookstore.entity.Payment;
import com.bookstore.onlinebookstore.entity.PaymentStatus;
import com.bookstore.onlinebookstore.repository.OrderRepository;
import com.bookstore.onlinebookstore.repository.PaymentRepository;
import com.bookstore.onlinebookstore.service.InvoiceService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public InvoiceController(
            InvoiceService invoiceService,
            OrderRepository orderRepository,
            PaymentRepository paymentRepository) {
        this.invoiceService = invoiceService;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable int orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException(
                    "Invoice available only after successful payment");
        }

        byte[] pdf = invoiceService.generateInvoice(order, payment);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice_" + orderId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
