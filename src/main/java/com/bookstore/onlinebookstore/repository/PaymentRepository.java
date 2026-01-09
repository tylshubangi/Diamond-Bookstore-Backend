package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrder_Id(int orderId);
}
