package com.example.local_service_booking.controllers;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.*;
import com.example.local_service_booking.services.UserProviderService;
import com.example.local_service_booking.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/providers")
public class ProviderController {

    private final Logger log = LoggerFactory.getLogger(ProviderController.class);
    private final UserProviderService userProviderService;

    public ProviderController(UserProviderService userProviderService) {
        this.userProviderService = userProviderService;
    }

    @PostMapping("/availability")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<String>> setAvailability(@RequestBody ProviderAvailabilityListDto availabilityDtos) {
        log.debug("setAvailability is called");

        AppUserDto user = UserUtils.getCurrentUser();
        userProviderService.setAvailability(user.getProviderProfile().getId(), availabilityDtos.getAvailabilityDtos());
        return ResponseEntity.ok(ApiResponse.success(1015, Constants.getMessage(1015), "OK"));
    }

    @GetMapping("/availability")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<List<ProviderAvailabilityDto>>> getAvailability(HttpServletRequest request) throws Exception {
        log.debug("getAvailability is called");

        AppUserDto user = UserUtils.getCurrentUser();
        List<ProviderAvailabilityDto> availabilities = userProviderService.getAvailability(user.getProviderProfile().getId());
        return ResponseEntity.ok(ApiResponse.success(1016, Constants.getMessage(1016), availabilities));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity<ApiResponse<ProviderProfileUpdateDto>> updateProviderProfile(HttpServletRequest httpServletRequest,
        @RequestBody ProviderProfileUpdateDto request) throws Exception {

        AppUserDto user = UserUtils.getCurrentUser();
        userProviderService.updateProviderProfile(user.getProviderProfile().getId(), request);
        return ResponseEntity.ok(ApiResponse.success(1017, Constants.getMessage(1017), request));
    }

}

