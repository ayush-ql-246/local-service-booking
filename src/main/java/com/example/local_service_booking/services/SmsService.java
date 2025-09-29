package com.example.local_service_booking.services;

public interface SmsService {
    void sendSms(String to, String subject, String body);
}
