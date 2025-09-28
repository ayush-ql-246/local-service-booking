package com.example.local_service_booking.dtos;

import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.entities.UserRoles;

public class UserResponseDto {
    private Long id;
    private String email;
    private UserRoles role;
    private ProviderProfileDto providerProfile;
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public ProviderProfileDto getProviderProfile() {
        return providerProfile;
    }

    public void setProviderProfile(ProviderProfileDto providerProfile) {
        this.providerProfile = providerProfile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static UserResponseDto from(AppUser user, ProviderProfile providerProfile, String token) {
        ProviderProfileDto profileDto = null;
        if (providerProfile != null) {
            profileDto = new ProviderProfileDto();
            profileDto.setId(providerProfile.getId());
            profileDto.setCity(providerProfile.getCity());
            profileDto.setStatus(providerProfile.getStatus());
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setProviderProfile(profileDto);
        dto.setToken(token);
        return dto;
    }

    public static UserResponseDto converter(AppUser user) {
        ProviderProfileDto profileDto = null;
        ProviderProfile providerProfile = user.getProviderProfile();
        if (providerProfile != null) {
            profileDto = new ProviderProfileDto();
            profileDto.setCity(providerProfile.getCity());
            profileDto.setStatus(providerProfile.getStatus());
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setProviderProfile(profileDto);
        return dto;
    }
}
