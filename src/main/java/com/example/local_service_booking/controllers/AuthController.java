package com.example.local_service_booking.controllers;

import com.example.local_service_booking.dtos.*;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.exceptions.UnauthorizedAccessException;
import com.example.local_service_booking.exceptions.UserAlreadyExistsException;
import com.example.local_service_booking.services.AuthService;
import com.example.local_service_booking.services.EmailService;
import com.example.local_service_booking.services.UserService;
import com.example.local_service_booking.utils.OtpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final EmailService emailService;

    public AuthController(AuthService authService, UserService userService, EmailService emailService) {
        this.authService = authService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> registerUser(@RequestBody UserRegistrationDto request) throws UserAlreadyExistsException {
        UserResponseDto user = authService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success(
                200,
                "The user was successfully created",
                user
        ));
    }

    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse<Map<String, Object>>> requestOtp(@RequestBody OtpRequestDto request) throws Exception {

        AppUser user = userService.getUserByEmail(request.getEmail());
        if(user.getStatus().equals(UserStatus.BLOCKED)) {
            throw new UnauthorizedAccessException("This account is blocked.");
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000); // 6-digit OTP
        long expiry = System.currentTimeMillis() + 5 * 60 * 1000; // 5 min expiry

        String token = OtpUtil.generateOtpToken(request.getEmail(), otp, expiry);

        String emailBody = "Hello " + user.getName() + ",\n\nYour OTP for login is: " + otp + "\nThis code is valid for 5 minutes.";
        emailService.sendEmail(request.getEmail(), "Your OTP Code", emailBody);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);

        return ResponseEntity.ok(ApiResponse.success(200, "OTP sent successfully", data));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequestDto request) throws Exception {
        AppUser user = userService.getUserByEmail(request.getEmail());
        if(user.getStatus().equals(UserStatus.BLOCKED)) {
            throw new UnauthorizedAccessException("This account is blocked.");
        }

        boolean valid = OtpUtil.verifyOtp(request.getEmail(), request.getOtp(), request.getToken());
        if (!valid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(401, "Invalid or expired OTP", Map.of("details", "OTP verification failed")));
        }

        return ResponseEntity.ok(ApiResponse.success(200, "Login successful", userService.getUserResponseByEmail(request.getEmail())));
    }


}
