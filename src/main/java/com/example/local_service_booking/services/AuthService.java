package com.example.local_service_booking.services;

import com.example.local_service_booking.dtos.UserRegistrationDto;
import com.example.local_service_booking.dtos.UserResponseDto;
import com.example.local_service_booking.exceptions.UserAlreadyExistsException;

public interface AuthService {
    public UserResponseDto registerUser(UserRegistrationDto request) throws UserAlreadyExistsException;
}
