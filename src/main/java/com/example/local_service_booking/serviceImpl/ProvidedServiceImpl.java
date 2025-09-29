package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.ProviderServiceDto;
import com.example.local_service_booking.dtos.ServiceRequestDto;
import com.example.local_service_booking.entities.ProviderAvailability;
import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.entities.ProviderService;
import com.example.local_service_booking.entities.ServiceCategory;
import com.example.local_service_booking.exceptions.InvalidServiceRequestException;
import com.example.local_service_booking.repositories.ProviderProfileRepository;
import com.example.local_service_booking.repositories.ProviderServiceRepository;
import com.example.local_service_booking.services.ProvidedService;
import com.example.local_service_booking.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProvidedServiceImpl implements ProvidedService {

    private final ProviderServiceRepository serviceRepository;
    private final UserService userService;
    private final ProviderProfileRepository providerProfileRepository;

    public ProvidedServiceImpl(ProviderServiceRepository serviceRepository,
                               UserService userService, ProviderProfileRepository providerProfileRepository) {
        this.serviceRepository = serviceRepository;
        this.userService = userService;
        this.providerProfileRepository = providerProfileRepository;
    }

    @Override
    public ServiceRequestDto createOrUpdateService(Long providerId, ServiceRequestDto serviceRequestDto) throws Exception {
        ProviderProfile provider = userService.getProviderProfileByProviderId(providerId);

        // Check if service with same category exists for this provider
        ProviderService providerService = serviceRepository.findByProviderAndCategory(provider, serviceRequestDto.getCategory())
                .map(existingService -> {
                    existingService.setDescription(serviceRequestDto.getDescription());
                    existingService.setPricingType(serviceRequestDto.getPricingType());
                    existingService.setBasePrice(serviceRequestDto.getBasePrice());
                    existingService.setIconUrl(serviceRequestDto.getIconUrl());
                    return serviceRepository.save(existingService);
                })
                .orElseGet(() -> {
                    // Create new service
                    ProviderService newService = new ProviderService();
                    newService.setProvider(provider);
                    newService.setCategory(serviceRequestDto.getCategory());
                    newService.setDescription(serviceRequestDto.getDescription());
                    newService.setPricingType(serviceRequestDto.getPricingType());
                    newService.setBasePrice(serviceRequestDto.getBasePrice());
                    newService.setIconUrl(serviceRequestDto.getIconUrl());
                    return serviceRepository.save(newService);
                });

        serviceRequestDto.setId(providerService.getId());
        return serviceRequestDto;
    }

    @Override
    public List<ProviderServiceDto> getAllServicesForProvider(Long providerId) throws Exception {
        ProviderProfile provider = userService.getProviderProfileByProviderId(providerId);
        List<ProviderService> providerServices = serviceRepository.findByProvider(provider);

        List<ProviderServiceDto> providerServiceDtoList = new ArrayList<>();
        for(ProviderService service : providerServices) {
            ProviderServiceDto serviceDto = new ProviderServiceDto();
            serviceDto.setId(service.getId());
            serviceDto.setBasePrice(service.getBasePrice());
            serviceDto.setCategory(service.getCategory());
            serviceDto.setDescription(service.getDescription());
            serviceDto.setIconUrl(service.getIconUrl());
            serviceDto.setPricingType(service.getPricingType());
            serviceDto.setCreatedAt(service.getCreatedAt());
            serviceDto.setUpdatedAt(service.getUpdatedAt());
            providerServiceDtoList.add(serviceDto);
        }

        return providerServiceDtoList;
    }

    @Override
    public void deleteService(Long providerId, Long serviceId) throws Exception {
        ProviderProfile provider = userService.getProviderProfileByProviderId(providerId);
        ProviderService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException(Constants.getMessage(2017)));

        // Ensure provider owns this service
        if (!service.getProvider().getId().equals(provider.getId())) {
            throw new RuntimeException(Constants.getMessage(2029));
        }
        serviceRepository.delete(service);
    }

    @Override
    public Page<ProviderService> getServicesByCategory(ServiceCategory category, Pageable pageable) {
        return serviceRepository.findByCategory(category, pageable);
    }

    @Override
    public Page<ProviderService> getAllServices(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }

    @Override
    public Page<ProviderService> getAllAvailableServices(ServiceCategory category, Long startTime, Long endTime, Pageable pageable) throws Exception {
        if(startTime>23 || startTime<0 || endTime<0 || endTime>23) {
            throw new InvalidServiceRequestException(Constants.getMessage(2030));
        }
        if(startTime>=endTime) {
            throw new InvalidServiceRequestException(Constants.getMessage(2031));
        }
        Page<ProviderService> services = serviceRepository.findAvailableServices(category, startTime, endTime, pageable);
        return services;
    }

}
