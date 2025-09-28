package com.example.local_service_booking.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
