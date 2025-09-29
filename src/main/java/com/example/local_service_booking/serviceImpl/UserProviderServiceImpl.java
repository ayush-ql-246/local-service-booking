package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.ProviderAvailabilityDto;
import com.example.local_service_booking.dtos.ProviderProfileUpdateDto;
import com.example.local_service_booking.entities.ProviderAvailability;
import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.exceptions.InvalidServiceRequestException;
import com.example.local_service_booking.repositories.ProviderAvailabilityRepository;
import com.example.local_service_booking.repositories.ProviderProfileRepository;
import com.example.local_service_booking.services.UserProviderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserProviderServiceImpl implements UserProviderService {

    private final ProviderAvailabilityRepository availabilityRepository;
    private final ProviderProfileRepository providerProfileRepository;

    public UserProviderServiceImpl(ProviderAvailabilityRepository availabilityRepository,
                                   ProviderProfileRepository providerProfileRepository) {
        this.availabilityRepository = availabilityRepository;
        this.providerProfileRepository = providerProfileRepository;
    }

    @Override
    public void setAvailability(Long providerId, List<ProviderAvailabilityDto> availabilityDtos) {
        ProviderProfile provider = providerProfileRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException(Constants.getMessage(2036)));

        for (ProviderAvailabilityDto dto : availabilityDtos) {
            availabilityRepository.findByProviderIdAndDayOfWeek(providerId, dto.getDayOfWeek())
                    .ifPresentOrElse(existing -> {
                        // Update existing entry
                        existing.setStartTime(dto.getStartTime());
                        existing.setEndTime(dto.getEndTime());
                        existing.setBreaks(dto.getBreaks());
                        availabilityRepository.save(existing);
                    }, () -> {
                        // Create new entry
                        ProviderAvailability newAvailability = new ProviderAvailability();
                        newAvailability.setProvider(provider);
                        newAvailability.setDayOfWeek(dto.getDayOfWeek());
                        newAvailability.setStartTime(dto.getStartTime());
                        newAvailability.setEndTime(dto.getEndTime());
                        newAvailability.setBreaks(dto.getBreaks());
                        availabilityRepository.save(newAvailability);
                    });
        }
    }

    @Override
    public List<ProviderAvailabilityDto> getAvailability(Long providerId) {
        ProviderProfile provider = providerProfileRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException(Constants.getMessage(2036)));
        List<ProviderAvailabilityDto> providerAvailabilityDtoList = new ArrayList<>();

        for(ProviderAvailability availability : provider.getAvailabilityList()) {
            ProviderAvailabilityDto providerAvailabilityDto = new ProviderAvailabilityDto();
            providerAvailabilityDto.setId(availability.getId());
            providerAvailabilityDto.setDayOfWeek(availability.getDayOfWeek());
            providerAvailabilityDto.setStartTime(availability.getStartTime());
            providerAvailabilityDto.setEndTime(availability.getEndTime());
            providerAvailabilityDto.setBreaks(availability.getBreaks());

            providerAvailabilityDtoList.add(providerAvailabilityDto);
        }
        return providerAvailabilityDtoList;
    }

    @Override
    public void updateProviderProfile(Long providerId, ProviderProfileUpdateDto request) throws Exception {
        ProviderProfile profile = providerProfileRepository.findById(providerId)
                .orElseThrow(() -> new InvalidServiceRequestException(Constants.getMessage(2037)));

        if (request.getCity() != null) profile.setCity(request.getCity());
        if (request.getStatus() != null) profile.setStatus(request.getStatus());

        providerProfileRepository.save(profile);
    }
}

