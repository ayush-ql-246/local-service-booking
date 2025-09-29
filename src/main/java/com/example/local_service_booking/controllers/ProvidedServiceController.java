package com.example.local_service_booking.controllers;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.ApiResponse;
import com.example.local_service_booking.dtos.AppUserDto;
import com.example.local_service_booking.dtos.ProviderServiceDto;
import com.example.local_service_booking.dtos.ServiceRequestDto;
import com.example.local_service_booking.exceptions.UnauthorizedAccessException;
import com.example.local_service_booking.services.ProvidedService;
import com.example.local_service_booking.utils.UserUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/provider/services")
public class ProvidedServiceController {

    private final ProvidedService providedService;

    public ProvidedServiceController(ProvidedService providerService) {
        this.providedService = providerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<ServiceRequestDto>> addOrUpdateService(@RequestBody ServiceRequestDto serviceRequestDto) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        ServiceRequestDto service = providedService.createOrUpdateService(user.getProviderProfile().getId(), serviceRequestDto);
        return ResponseEntity.ok(ApiResponse.success(1012, Constants.getMessage(1012), service));
    }

    @GetMapping
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<List<ProviderServiceDto>>> getAllServices() throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        List<ProviderServiceDto> services = providedService.getAllServicesForProvider(user.getProviderProfile().getId());
        return ResponseEntity.ok(ApiResponse.success(1013, Constants.getMessage(1013), services));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<String>> deleteService(@PathVariable Long id) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        providedService.deleteService(user.getProviderProfile().getId(), id);
        return ResponseEntity.ok(ApiResponse.success(1014, Constants.getMessage(1014), "Deleted"));
    }
}

