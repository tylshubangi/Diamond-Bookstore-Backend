package com.bookstore.onlinebookstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDTO {

    @NotNull
    private Integer orderId;

    @NotNull
    private String paymentMethod; // CARD / DEBIT_CARD / CREDIT_CARD

    // optional for mock
    private String cardNumber;
    private String cardHolderName;
    private String expiry;
    private String cvv;
}
