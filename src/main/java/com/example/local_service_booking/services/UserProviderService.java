package com.example.local_service_booking.services;

import com.example.local_service_booking.dtos.ProviderAvailabilityDto;
import com.example.local_service_booking.dtos.ProviderProfileUpdateDto;
import com.example.local_service_booking.entities.ProviderAvailability;
import com.example.local_service_booking.entities.ProviderProfile;

import java.util.List;

public interface UserProviderService {
    void setAvailability(Long providerId, List<ProviderAvailabilityDto> availabilityDtos);

    List<ProviderAvailabilityDto> getAvailability(Long providerId);

    void updateProviderProfile(Long providerId, ProviderProfileUpdateDto request) throws Exception;
}
