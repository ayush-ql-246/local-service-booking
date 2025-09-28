package com.example.local_service_booking.repositories;

import com.example.local_service_booking.entities.Booking;
import com.example.local_service_booking.entities.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByUserId(Long userId, Pageable pageable);

    Page<Booking> findByProviderId(Long providerId, Pageable pageable);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

    Page<Booking> findByStatusAndProviderId(BookingStatus status, Long providerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.provider.id = :providerId " +
            "AND b.status IN ('REQUESTED', 'ACCEPTED') " +
            "AND (b.startTime < :endTime AND b.endTime > :startTime)")
    List<Booking> findOverlappingBookingsForProvider(@Param("providerId") Long providerId,
                                                     @Param("startTime") Long startTime,
                                                     @Param("endTime") Long endTime);

}
