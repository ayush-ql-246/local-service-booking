package com.example.local_service_booking.exceptions;

import com.example.local_service_booking.dtos.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }

    @RestControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<ApiResponse<Object>> handleUserExists(UserAlreadyExistsException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.failure(
                            400,
                            "User already exists",
                            Map.of("details", ex.getMessage())
                    ));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
            return ResponseEntity
                    .status(500)
                    .body(ApiResponse.failure(
                            500,
                            "An unexpected error occurred",
                            Map.of("details", ex.getMessage())
                    ));
        }
    }
}
