package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.UserResponseDto;
import com.example.local_service_booking.dtos.UserStatus;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.entities.UserRoles;
import com.example.local_service_booking.exceptions.InvalidServiceRequestException;
import com.example.local_service_booking.repositories.AppUserRepository;
import com.example.local_service_booking.services.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final AppUserRepository userRepository;

    public AdminServiceImpl(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserResponseDto> getAllUsers(UserRoles role, int page, int size) throws Exception {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<AppUser> users;
        if (role != null) {
            users = userRepository.findByRole(role, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(UserResponseDto::converter);
    }

    @Override
    public void updateUserStatus(Long id, UserStatus status) throws Exception {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Constants.getMessage(2007)));

        if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
            throw new InvalidServiceRequestException(Constants.getMessage(2010));
        }

        user.setStatus(status);
        userRepository.save(user);
    }

}

