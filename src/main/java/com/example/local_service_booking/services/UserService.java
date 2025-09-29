package com.example.local_service_booking.services;

import com.example.local_service_booking.dtos.UserProfileUpdateDto;
import com.example.local_service_booking.dtos.UserResponseDto;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.entities.ProviderProfile;

public interface UserService {

    public UserResponseDto getUserResponseByEmail(String email) throws Exception;

    AppUser getUserByEmail(String email) throws Exception;

    AppUser getUserByPhoneNumber(String phoneNo) throws Exception;

    ProviderProfile getProviderProfileByProviderId(Long providerId) throws Exception;

    void updateUserProfile(Long userId, UserProfileUpdateDto request) throws Exception;
}
