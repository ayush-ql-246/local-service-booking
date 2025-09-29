package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.TokenDto;
import com.example.local_service_booking.dtos.UserProfileUpdateDto;
import com.example.local_service_booking.dtos.UserResponseDto;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.exceptions.InvalidServiceRequestException;
import com.example.local_service_booking.exceptions.UserNotFoundException;
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
    public UserResponseDto getUserResponseByEmail(String email) throws Exception {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(Constants.getMessage(2007)));

        TokenDto tokenDto = new TokenDto();
        tokenDto.setEmail(user.getEmail());
        tokenDto.setRole(user.getRole());
        String jwtToken = jwtService.generateToken(tokenDto);
        return UserResponseDto.from(user, user.getProviderProfile(), jwtToken);
    }

    @Override
    public AppUser getUserByEmail(String email) throws Exception {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(Constants.getMessage(2007)));
    }

    @Override
    public AppUser getUserByPhoneNumber(String phoneNo) throws Exception {
        return userRepository.findByPhoneNumber(phoneNo)
                .orElseThrow(() -> new UserNotFoundException(Constants.getMessage(2007)));
    }

    @Override
    public ProviderProfile getProviderProfileByProviderId(Long providerId) throws Exception {
        return providerProfileRepository.findById(providerId)
                .orElseThrow(() -> new UserNotFoundException(Constants.getMessage(2008)));
    }

    @Override
    public void updateUserProfile(Long userId, UserProfileUpdateDto request) throws Exception {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidServiceRequestException(Constants.getMessage(2007)));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhoneNumber() != null) {
            if(request.getPhoneNumber().length()!=10) {
                throw new UserValidationException(Constants.getMessage(2009));
            }
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) user.setAddress(request.getAddress());

        userRepository.save(user);
    }
}