package com.example.local_service_booking.services;

import com.example.local_service_booking.dtos.RatingDto;
import com.example.local_service_booking.dtos.RatingRequestDto;

public interface RatingService {
    RatingDto addRating(Long bookingId, RatingRequestDto request, Long userId) throws Exception;
}
