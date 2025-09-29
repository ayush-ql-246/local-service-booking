package com.example.local_service_booking.controllers;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.*;
import com.example.local_service_booking.entities.BookingStatus;
import com.example.local_service_booking.entities.UserRoles;
import com.example.local_service_booking.services.AdminService;
import com.example.local_service_booking.services.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final BookingService bookingService;

    public AdminController(AdminService adminService, BookingService bookingService) {
        this.adminService = adminService;
        this.bookingService = bookingService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllUsers(
            @RequestParam(value = "role", required = false) UserRoles role,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {

        Page<UserResponseDto> users = adminService.getAllUsers(role, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("bookings", users.getContent());
        response.put("currentPage", users.getNumber());
        response.put("totalItems", users.getTotalElements());
        response.put("totalPages", users.getTotalPages());
        return ResponseEntity.ok(ApiResponse.success(1025, Constants.getMessage(1025), response));
    }

    @PutMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateUserStatus(HttpServletRequest httpServletRequest, @PathVariable Long id,
        @RequestParam("status") UserStatus status) throws Exception {

        adminService.updateUserStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(1026, Constants.getMessage(1026), "Status changed to " + status));
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllBookings(
        @RequestParam(value = "status", required = false) BookingStatus status,
        @RequestParam(value = "providerId", required = false) Long providerId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {

        Page<BookingDto> bookings = bookingService.getAllBookings(status, providerId, page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("bookings", bookings.getContent());
        response.put("currentPage", bookings.getNumber());
        response.put("totalItems", bookings.getTotalElements());
        response.put("totalPages", bookings.getTotalPages());
        return ResponseEntity.ok(ApiResponse.success(1027, Constants.getMessage(1027), response));
    }

}
