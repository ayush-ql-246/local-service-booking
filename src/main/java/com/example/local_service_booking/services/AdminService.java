package com.example.local_service_booking.services;

import com.example.local_service_booking.dtos.UserResponseDto;
import com.example.local_service_booking.dtos.UserStatus;
import com.example.local_service_booking.entities.UserRoles;
import org.springframework.data.domain.Page;

public interface AdminService {
    Page<UserResponseDto> getAllUsers(UserRoles role, int page, int size) throws Exception;

    void updateUserStatus(Long id, UserStatus status) throws Exception;
}
