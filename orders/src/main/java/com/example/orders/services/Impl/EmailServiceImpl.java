package com.example.orders.services.Impl;

import com.example.orders.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendOrderConfirmation(String toEmail, String orderId) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject("Order Confirmation - " + orderId);
        simpleMailMessage.setText("Thank you for your order! Your order ID is: " + orderId);

        javaMailSender.send(simpleMailMessage);
        log.info("Mail sent to the recipient with email: " + toEmail);
    }
}
