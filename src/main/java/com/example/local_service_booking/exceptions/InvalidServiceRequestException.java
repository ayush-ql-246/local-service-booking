package com.example.local_service_booking.exceptions;

public class InvalidServiceRequestException extends Exception {
    public InvalidServiceRequestException(String msg) {
        super(msg);
    }
}
