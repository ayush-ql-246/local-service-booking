package com.example.local_service_booking.controllers;


import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.ApiResponse;
import com.example.local_service_booking.dtos.AppUserDto;
import com.example.local_service_booking.dtos.UserProfileUpdateDto;
import com.example.local_service_booking.exceptions.UnauthorizedAccessException;
import com.example.local_service_booking.services.UserService;
import com.example.local_service_booking.utils.UserUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUser")
    public ResponseEntity<ApiResponse<AppUserDto>> getUser() throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(401));
        }

        return ResponseEntity.ok(ApiResponse.success(1019, Constants.getMessage(1019), user));
    }


    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserProfileUpdateDto>> updateUserProfile(@RequestBody UserProfileUpdateDto userProfileUpdateDto) throws Exception {
        AppUserDto user = UserUtils.getCurrentUser();
        if(user==null) {
            throw new UnauthorizedAccessException(Constants.getMessage(401));
        }

        userService.updateUserProfile(user.getId(), userProfileUpdateDto);
        return ResponseEntity.ok(ApiResponse.success(1020, Constants.getMessage(1020), userProfileUpdateDto));
    }
}
