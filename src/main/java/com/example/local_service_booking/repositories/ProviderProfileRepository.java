package com.example.local_service_booking.repositories;

import com.example.local_service_booking.entities.ProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {
}
