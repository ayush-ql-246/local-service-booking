package com.example.local_service_booking.controllers;

import com.example.local_service_booking.constants.Constants;
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
            throw new UnauthorizedAccessException(Constants.getMessage(401));
        }
        if(!user.getId().equals(request.getUserId())) {
            throw new InvalidServiceRequestException(Constants.getMessage(2006));
        }

        BookingDto booking = bookingService.createBooking(request);
        return ResponseEntity.ok(ApiResponse.success(1005, Constants.getMessage(1005), booking));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserBookings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();

        if(user==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(401));
        }
        Map<String, Object> response = bookingService.getBookingsByUser(user.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success(1006, Constants.getMessage(1006), response));
    }

    @GetMapping("/provider")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProviderBookings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();

        if(user==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(401));
        }

        Map<String, Object> response = bookingService.getBookingsByProvider(user.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success(1007, Constants.getMessage(1007), response));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDto>> getBookingById(@PathVariable Long bookingId) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();

        if(user==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(401));
        }
        BookingDto bookingDetails = bookingService.getBookingById(bookingId, user);
        return ResponseEntity.ok(ApiResponse.success(1008, Constants.getMessage(1008), bookingDetails));
    }


    @PutMapping("/{bookingId}/status")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<BookingDto>> updateBookingStatus(@PathVariable Long bookingId,
        @RequestBody BookingStatusUpdateDto request) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(401));
        }

        BookingDto updatedBooking = bookingService.updateBookingStatus(user.getId(), bookingId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success(1009, Constants.getMessage(1009), updatedBooking));
    }

    @PutMapping("/{bookingId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<BookingDto>> cancelBooking(HttpServletRequest httpServletRequest, @PathVariable Long bookingId) throws Exception {

        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(401));
        }
        BookingDto cancelledBooking = bookingService.cancelBooking(bookingId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(1010, Constants.getMessage(1010), cancelledBooking));
    }

    @PostMapping("/{bookingId}/rating")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<RatingDto>> addRating(HttpServletRequest httpServletRequest,
        @PathVariable Long bookingId, @RequestBody RatingRequestDto request) throws Exception {

        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(401));
        }

        RatingDto rating = ratingService.addRating(bookingId, request, user.getId());
        return ResponseEntity.ok(ApiResponse.success(1011, Constants.getMessage(1011), rating));
    }

}
