package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.services.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);


    @Override
    public void sendSms(String to, String subject, String body) {
        try {
            log.info("Sms: {}", body);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send sms", e);
        }
    }
}
