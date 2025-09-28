package com.example.local_service_booking.repositories;

import com.example.local_service_booking.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByBookingId(Long bookingId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Rating r WHERE r.provider.id = :providerId")
    Double getAverageRatingByProvider(@Param("providerId") Long providerId);
}
