package com.bookstore.onlinebookstore.dto;

public class PaymentResponseDTO {

    private String status;
    private String message;
    private Integer paymentId;

    public PaymentResponseDTO() {
    }

    public PaymentResponseDTO(String status, String message, Integer paymentId) {
        this.status = status;
        this.message = message;
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
}
