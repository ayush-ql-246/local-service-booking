package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.dtos.TokenDto;
import com.example.local_service_booking.dtos.UserRegistrationDto;
import com.example.local_service_booking.dtos.UserResponseDto;
import com.example.local_service_booking.dtos.UserStatus;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.entities.UserRoles;
import com.example.local_service_booking.exceptions.UserAlreadyExistsException;
import com.example.local_service_booking.repositories.AppUserRepository;
import com.example.local_service_booking.repositories.ProviderProfileRepository;
import com.example.local_service_booking.services.AuthService;
import com.example.local_service_booking.services.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AppUserRepository userRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final JwtService jwtService;

    public AuthServiceImpl(AppUserRepository userRepository,
                           ProviderProfileRepository providerProfileRepository,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.providerProfileRepository = providerProfileRepository;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRegistrationDto request) throws UserAlreadyExistsException {
        if (request.getRole() == UserRoles.ADMIN) {
            throw new IllegalArgumentException("Cannot create admin accounts via registration");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Phone number already in use");
        }

        // 1. Create user
        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        // 2. If provider, create providerProfile
        ProviderProfile providerProfile = null;
        if (request.getRole() == UserRoles.PROVIDER) {
            providerProfile = new ProviderProfile();
            providerProfile.setUser(user);
//            providerProfile.setServiceTypes(request.getProviderProfile().getServiceTypes());
            providerProfile.setCity(request.getProviderProfile().getCity());
            providerProfile.setStatus(request.getProviderProfile().getStatus());
            providerProfile.setRatingAverage(0.0);
            providerProfileRepository.save(providerProfile);
        }

        TokenDto tokenDto = new TokenDto();
        tokenDto.setEmail(user.getEmail());
        tokenDto.setId(user.getId());
        tokenDto.setRole(user.getRole());
        String token = jwtService.generateToken(tokenDto);
        return UserResponseDto.from(user, providerProfile, token);
    }

}
