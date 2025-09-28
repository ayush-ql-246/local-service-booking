package com.example.local_service_booking.services;

import com.example.local_service_booking.dtos.AppUserDto;
import com.example.local_service_booking.dtos.TokenDto;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
    public String generateToken (TokenDto tokenDto);

    AppUserDto getUserFromToken(HttpServletRequest request) throws Exception;

    String extractEmail(String token);

    String getJwtFromRequest(HttpServletRequest request);
}
