package com.example.local_service_booking.controllers;

import com.example.local_service_booking.dtos.*;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.exceptions.UnauthorizedAccessException;
import com.example.local_service_booking.exceptions.UserAlreadyExistsException;
import com.example.local_service_booking.services.AuthService;
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

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
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

        String hash = authService.sendLoginOtp(request);
        if(hash==null) {
            throw new UnauthorizedAccessException("Hash token not generated");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("token", hash);

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
