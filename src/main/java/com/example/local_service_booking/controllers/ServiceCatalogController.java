package com.example.local_service_booking.controllers;

import com.example.local_service_booking.dtos.ApiResponse;
import com.example.local_service_booking.dtos.AppUserDto;
import com.example.local_service_booking.dtos.ProviderServiceDto;
import com.example.local_service_booking.entities.ProviderService;
import com.example.local_service_booking.entities.ServiceCategory;
import com.example.local_service_booking.exceptions.UnauthorizedAccessException;
import com.example.local_service_booking.services.ProvidedService;
import com.example.local_service_booking.utils.DtoUtils;
import com.example.local_service_booking.utils.UserUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/services/catalog")
public class ServiceCatalogController {

    private final ProvidedService providedService;

    public ServiceCatalogController(ProvidedService providedService) {
        this.providedService = providedService;
    }

    /**
     * Get all services with optional category filter
     * Example: GET /api/v1/services?category=PLUMBING
     */

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Object>> getAllServices(@RequestParam(value = "category", required = false) ServiceCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {

        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ProviderService> services;

        if (category != null) {
            services = providedService.getServicesByCategory(category, pageable);
        } else {
            services = providedService.getAllServices(pageable);
        }

        List<ProviderServiceDto> catalogServices = DtoUtils.mapProviderServicesToDto(services.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("content", catalogServices);
        response.put("currentPage", services.getNumber());
        response.put("totalItems", services.getTotalElements());
        response.put("totalPages", services.getTotalPages());

        return ResponseEntity.ok(ApiResponse.success(200, "Services fetched successfully", response));
    }

    @GetMapping("/available-services")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Object>> getAllAvailableServices(
        @RequestParam ServiceCategory category,
        @RequestParam Long startTime,
        @RequestParam Long endTime,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) throws Exception {

        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ProviderService> services = providedService.getAllAvailableServices(category, startTime, endTime, pageable);

        List<ProviderServiceDto> availableProviderServices = DtoUtils.mapProviderServicesToDto(services.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("content", availableProviderServices);
        response.put("currentPage", services.getNumber());
        response.put("totalItems", services.getTotalElements());
        response.put("totalPages", services.getTotalPages());

        return ResponseEntity.ok(ApiResponse.success(200, "Available services fetched successfully", response));
    }


}
