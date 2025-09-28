package com.example.local_service_booking.exceptions;

public class UnauthorizedAccessException extends Exception {

    public UnauthorizedAccessException(String msg) {
        super(msg);
    }
}
