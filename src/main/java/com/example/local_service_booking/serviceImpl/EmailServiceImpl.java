package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    public EmailServiceImpl() { }

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            log.info("Email: {}", body);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
