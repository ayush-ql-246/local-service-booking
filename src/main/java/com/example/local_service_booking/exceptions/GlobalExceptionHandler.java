package com.example.local_service_booking.exceptions;

import com.example.local_service_booking.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistException(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(400, "User Already Exists", Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidUserException(InvalidUserException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.failure(401, "Unauthorized access", Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.failure(401, "Unauthorized access", Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(InvalidServiceRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidServiceRequestException(InvalidServiceRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(400, "Wrong data provided", Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ApiResponse<Object>> handleBookingException(BookingException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(400, "Booking related error occurred", Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserValidationException(UserValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(400, "User validation error occurred", Map.of("details", ex.getMessage())));
    }

    // Generic fallback for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(500, "Internal Server Error", Map.of("detials", "An error occurred.")));
    }
}

