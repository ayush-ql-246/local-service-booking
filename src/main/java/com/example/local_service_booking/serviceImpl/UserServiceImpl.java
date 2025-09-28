package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.dtos.TokenDto;
import com.example.local_service_booking.dtos.UserProfileUpdateDto;
import com.example.local_service_booking.dtos.UserResponseDto;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.exceptions.InvalidServiceRequestException;
import com.example.local_service_booking.exceptions.UserValidationException;
import com.example.local_service_booking.repositories.AppUserRepository;
import com.example.local_service_booking.repositories.ProviderProfileRepository;
import com.example.local_service_booking.services.JwtService;
import com.example.local_service_booking.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final AppUserRepository userRepository;
    private final JwtService jwtService;
    private final ProviderProfileRepository providerProfileRepository;

    public UserServiceImpl(AppUserRepository userRepository, JwtService jwtService, ProviderProfileRepository providerProfileRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.providerProfileRepository = providerProfileRepository;
    }

    @Override
    public UserResponseDto getUserResponseByEmail(String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TokenDto tokenDto = new TokenDto();
        tokenDto.setEmail(user.getEmail());
        tokenDto.setRole(user.getRole());
        String jwtToken = jwtService.generateToken(tokenDto);
        return UserResponseDto.from(user, user.getProviderProfile(), jwtToken);
    }

    @Override
    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public ProviderProfile getProviderProfileByProviderId(Long providerId) {
        return providerProfileRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Override
    public void updateUserProfile(Long userId, UserProfileUpdateDto request) throws Exception {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidServiceRequestException("User not found"));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhoneNumber() != null) {
            if(request.getPhoneNumber().length()!=10) {
                throw new UserValidationException("Phone number should be of 10 characters");
            }
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) user.setAddress(request.getAddress());

        userRepository.save(user);
    }
}