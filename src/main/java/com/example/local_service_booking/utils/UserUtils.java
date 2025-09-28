package com.example.local_service_booking.utils;

import com.example.local_service_booking.dtos.AppUserDto;
import com.example.local_service_booking.dtos.ProviderProfileDto;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.entities.UserRoles;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    public static AppUserDto getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof AppUser user) {
            AppUserDto dto = new AppUserDto();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setName(user.getName());
            dto.setRole(user.getRole());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setAddress(user.getAddress());

            if (user.getRole() == UserRoles.PROVIDER && user.getProviderProfile() != null) {
                ProviderProfile profile = user.getProviderProfile();
                ProviderProfileDto providerProfileDto = new ProviderProfileDto();
                providerProfileDto.setId(profile.getId());
                providerProfileDto.setCity(profile.getCity());
                providerProfileDto.setStatus(profile.getStatus());
                dto.setProviderProfile(providerProfileDto);
            }

            return dto;
        }

        return null;
    }
}

