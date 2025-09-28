package com.example.local_service_booking.services;

import com.example.local_service_booking.dtos.ProviderServiceDto;
import com.example.local_service_booking.dtos.ServiceRequestDto;
import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.entities.ProviderService;
import com.example.local_service_booking.entities.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProvidedService {
    ServiceRequestDto createOrUpdateService(Long providerId, ServiceRequestDto serviceRequestDto);

    List<ProviderServiceDto> getAllServicesForProvider(Long providerId);

    void deleteService(Long providerId, Long serviceId);

    Page<ProviderService> getServicesByCategory(ServiceCategory category, Pageable pageable);

    Page<ProviderService> getAllServices(Pageable pageable);

    Page<ProviderService> getAllAvailableServices(ServiceCategory category, Long startTime, Long endTime, Pageable pageable) throws Exception;
}
