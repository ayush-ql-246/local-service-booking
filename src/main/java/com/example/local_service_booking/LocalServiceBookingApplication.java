package com.example.local_service_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.local_service_booking")
public class LocalServiceBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocalServiceBookingApplication.class, args);
	}

}
