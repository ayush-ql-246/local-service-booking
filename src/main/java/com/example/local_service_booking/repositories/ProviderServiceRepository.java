package com.example.local_service_booking.repositories;

import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.entities.ProviderService;
import com.example.local_service_booking.entities.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProviderServiceRepository extends JpaRepository<ProviderService, Long> {

    Page<ProviderService> findByCategory(ServiceCategory category, Pageable pageable);

    Optional<ProviderService> findByProviderAndCategory(ProviderProfile provider, ServiceCategory category);

    List<ProviderService> findByProvider(ProviderProfile provider);

    @Query("SELECT s FROM ProviderService s " +
            "JOIN s.provider p " +
            "JOIN p.availabilityList a " +
            "WHERE s.category = :category " +
            "AND p.status = 'AVAILABLE' " +
            "AND a.startTime <= :startTime " +
            "AND a.endTime >= :endTime")
    Page<ProviderService> findAvailableServices(
            @Param("category") ServiceCategory category,
            @Param("startTime") Long startTime,
            @Param("endTime") Long endTime,
            Pageable pageable
    );

}