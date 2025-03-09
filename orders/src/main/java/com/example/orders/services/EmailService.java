package com.example.orders.services;

public interface EmailService {
    public void sendOrderConfirmation(String toEmail, String orderId);
}
