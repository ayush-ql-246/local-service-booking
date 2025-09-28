package com.example.local_service_booking.controllers;

import com.example.local_service_booking.dtos.*;
import com.example.local_service_booking.exceptions.InvalidServiceRequestException;
import com.example.local_service_booking.exceptions.UnauthorizedAccessException;
import com.example.local_service_booking.services.BookingService;
import com.example.local_service_booking.services.RatingService;
import com.example.local_service_booking.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final RatingService ratingService;

    public BookingController(BookingService bookingService, RatingService ratingService) {
        this.bookingService = bookingService;
        this.ratingService = ratingService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<BookingDto>> createBooking(@RequestBody BookingRequestDto request) throws Exception {

        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        if(!user.getId().equals(request.getUserId())) {
            throw new InvalidServiceRequestException("User can create booking only for their accounts");
        }

        BookingDto booking = bookingService.createBooking(request);
        return ResponseEntity.ok(ApiResponse.success(200, "Booking created successfully", booking));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserBookings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();

        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        Map<String, Object> response = bookingService.getBookingsByUser(user.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success(200, "Provider bookings fetched successfully", response));
    }

    @GetMapping("/provider")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProviderBookings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();

        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        Map<String, Object> response = bookingService.getBookingsByProvider(user.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success(200, "Provider bookings fetched successfully", response));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDto>> getBookingById(@PathVariable Long bookingId) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();

        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        BookingDto bookingDetails = bookingService.getBookingById(bookingId, user);
        return ResponseEntity.ok(ApiResponse.success(200, "Booking details fetched successfully", bookingDetails));
    }


    @PutMapping("/{bookingId}/status")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<BookingDto>> updateBookingStatus(HttpServletRequest httpServletRequest, @PathVariable Long bookingId,
        @RequestBody BookingStatusUpdateDto request) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        BookingDto updatedBooking = bookingService.updateBookingStatus(user.getId(), bookingId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(200, "Booking status updated successfully", updatedBooking));
    }

    @PutMapping("/{bookingId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<BookingDto>> cancelBooking(HttpServletRequest httpServletRequest, @PathVariable Long bookingId) throws Exception {

        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        BookingDto cancelledBooking = bookingService.cancelBooking(bookingId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(200, "Booking cancelled successfully", cancelledBooking));
    }

    @PostMapping("/{bookingId}/rating")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<RatingDto>> addRating(HttpServletRequest httpServletRequest,
        @PathVariable Long bookingId, @RequestBody RatingRequestDto request) throws Exception {

        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        RatingDto rating = ratingService.addRating(bookingId, request, user.getId());
        return ResponseEntity.ok(ApiResponse.success(200, "Rating added successfully", rating));
    }

}
