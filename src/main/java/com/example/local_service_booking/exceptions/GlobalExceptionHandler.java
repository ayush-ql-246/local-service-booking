package com.example.local_service_booking.exceptions;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                .body(ApiResponse.failure(1021, Constants.getMessage(1021), Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidUserException(InvalidUserException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.failure(401, Constants.getMessage(401), Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.failure(401, Constants.getMessage(401), Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.failure(401, Constants.getMessage(401), Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(InvalidServiceRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidServiceRequestException(InvalidServiceRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(1022, Constants.getMessage(1022), Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(1022, Constants.getMessage(1022), Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ApiResponse<Object>> handleBookingException(BookingException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(1023, Constants.getMessage(1023), Map.of("details", ex.getMessage())));
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserValidationException(UserValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(1024, Constants.getMessage(1024), Map.of("details", ex.getMessage())));
    }

    // Generic fallback for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(500, Constants.getMessage(500), Map.of("detials", "An error occurred.")));
    }
}

