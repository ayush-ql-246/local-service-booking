package com.example.local_service_booking.repositories;

import com.example.local_service_booking.entities.DayOfWeekEnum;
import com.example.local_service_booking.entities.ProviderAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProviderAvailabilityRepository extends JpaRepository<ProviderAvailability, Long> {
    Optional<ProviderAvailability> findByProviderIdAndDayOfWeek(Long providerId, DayOfWeekEnum dayOfWeek);

}
