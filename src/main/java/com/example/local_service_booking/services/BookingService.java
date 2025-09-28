package com.example.local_service_booking.services;

import com.example.local_service_booking.dtos.AppUserDto;
import com.example.local_service_booking.dtos.BookingDto;
import com.example.local_service_booking.dtos.BookingRequestDto;
import com.example.local_service_booking.entities.Booking;
import com.example.local_service_booking.entities.BookingStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface BookingService {
    BookingDto createBooking(BookingRequestDto request) throws Exception;

    Map<String, Object> getBookingsByUser(Long userId, Integer page, Integer size);

    Map<String, Object> getBookingsByProvider(Long providerId, Integer page, Integer size);

    BookingDto getBookingById(Long bookingId, AppUserDto userDto) throws Exception;

    BookingDto updateBookingStatus(Long providerId, Long bookingId, BookingStatus status) throws Exception;

    BookingDto cancelBooking(Long bookingId, Long userId) throws Exception;

    Page<BookingDto> getAllBookings(BookingStatus status, Long providerId, int page, int size);
}
