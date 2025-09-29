package com.example.local_service_booking.controllers;

import com.example.local_service_booking.constants.Constants;
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
    private final OtpUtil otpUtil;

    public AuthController(AuthService authService, UserService userService, OtpUtil otpUtil) {
        this.authService = authService;
        this.userService = userService;
        this.otpUtil = otpUtil;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> registerUser(@RequestBody UserRegistrationDto request) throws UserAlreadyExistsException {
        UserResponseDto user = authService.registerUser(request);
        return ResponseEntity.ok(ApiResponse.success(
                1001,
                Constants.getMessage(1001),
                user
        ));
    }

    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse<Map<String, Object>>> requestOtp(@RequestBody OtpRequestDto request) throws Exception {

        String hash = authService.sendLoginOtp(request);
        if(hash==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(2001));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("token", hash);

        return ResponseEntity.ok(ApiResponse.success(1002, Constants.getMessage(1002), data));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequestDto request) throws Exception {
        if(request.getEmail()==null && request.getPhoneNumber()==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(2002));
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
        boolean valid = otpUtil.verifyOtp(loginViaEmail ? request.getEmail() : request.getPhoneNumber(), request.getOtp(), request.getToken());
        if (!valid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(1003, Constants.getMessage(1003), Map.of("details", Constants.getMessage(2005))));
        }

        return ResponseEntity.ok(ApiResponse.success(1004, Constants.getMessage(1004), userService.getUserResponseByEmail(user.getEmail())));
    }


}
