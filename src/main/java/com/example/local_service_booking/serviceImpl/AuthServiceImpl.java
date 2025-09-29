package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.*;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.entities.UserRoles;
import com.example.local_service_booking.exceptions.UnauthorizedAccessException;
import com.example.local_service_booking.exceptions.UserAlreadyExistsException;
import com.example.local_service_booking.repositories.AppUserRepository;
import com.example.local_service_booking.repositories.ProviderProfileRepository;
import com.example.local_service_booking.services.*;
import com.example.local_service_booking.utils.OtpUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AppUserRepository userRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final JwtService jwtService;
    private final UserService userService;
    private final EmailService emailService;
    private final SmsService smsService;
    private final OtpUtil otpUtil;

    public AuthServiceImpl(AppUserRepository userRepository,
                           ProviderProfileRepository providerProfileRepository,
                           JwtService jwtService, UserService userService,
                           SmsService smsService, EmailService emailService,
                           OtpUtil otpUtil) {
        this.userRepository = userRepository;
        this.providerProfileRepository = providerProfileRepository;
        this.jwtService = jwtService;
        this.userService = userService;
        this.emailService = emailService;
        this.smsService = smsService;
        this.otpUtil = otpUtil;
    }

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRegistrationDto request) throws UserAlreadyExistsException {
        if (request.getRole() == UserRoles.ADMIN) {
            throw new IllegalArgumentException(Constants.getMessage(2011));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(Constants.getMessage(2012));
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserAlreadyExistsException(Constants.getMessage(2013));
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

    @Override
    public String sendLoginOtp(OtpRequestDto request) throws Exception {
        if(request.getEmail()==null && request.getPhoneNumber()==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(2014));
        }

        boolean loginViaEmail = (request.getEmail() !=null && !request.getEmail().isEmpty());

        AppUser user = null;
        if(loginViaEmail) {
            user = userService.getUserByEmail(request.getEmail());
        } else {
            user = userService.getUserByPhoneNumber(request.getPhoneNumber());
        }
        if(user==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(2003));
        }
        if(user.getStatus().equals(UserStatus.BLOCKED)) {
            throw new UnauthorizedAccessException(Constants.getMessage(2004));
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000); // 6-digit OTP
        long expiry = System.currentTimeMillis() + 5 * 60 * 1000; // 5 min expiry

        String token = otpUtil.generateOtpToken(loginViaEmail ? request.getEmail() : request.getPhoneNumber(), otp, expiry);

        if(loginViaEmail) {
            String emailBody = "Hello " + user.getName() + ",\n\nYour OTP for login is: " + otp + "\nThis code is valid for 5 minutes.";
            emailService.sendEmail(request.getEmail(), "Your OTP Code", emailBody);
        } else {
            String smsBody = "Hello " + user.getName() + ",\n\nYour OTP for login is: " + otp + "\nThis code is valid for 5 minutes.";
            smsService.sendSms(request.getPhoneNumber(), "Your OTP Code", smsBody);
        }

        return token;
    }
}
