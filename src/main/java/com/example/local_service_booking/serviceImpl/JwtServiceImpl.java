package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.AppUserDto;
import com.example.local_service_booking.dtos.ProviderProfileDto;
import com.example.local_service_booking.dtos.TokenDto;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.entities.ProviderProfile;
import com.example.local_service_booking.entities.UserRoles;
import com.example.local_service_booking.exceptions.InvalidUserException;
import com.example.local_service_booking.services.JwtService;
import com.example.local_service_booking.services.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private final UserService userService;
    private Key key;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public JwtServiceImpl(@Lazy UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public String generateToken(TokenDto tokenDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", tokenDto.getId());
        claims.put("role", tokenDto.getRole());
        claims.put("email", tokenDto.getEmail());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(tokenDto.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public AppUserDto getUserFromToken(HttpServletRequest request) throws Exception {
        String jwtToken = getJwtFromRequest(request);
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new InvalidUserException(Constants.getMessage(2027));
        }

        String email = extractEmail(jwtToken);
        if (email == null) {
            throw new InvalidUserException(Constants.getMessage(2028));
        }

        AppUser user = userService.getUserByEmail(email);

        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setId(user.getId());
        appUserDto.setName(user.getName());
        appUserDto.setEmail(user.getEmail());
        appUserDto.setRole(user.getRole());
        appUserDto.setAddress(user.getAddress());
        appUserDto.setPhoneNumber(user.getPhoneNumber());

        if (user.getRole().equals(UserRoles.PROVIDER)) {
            ProviderProfile providerProfile = user.getProviderProfile();
            ProviderProfileDto providerProfileDto = new ProviderProfileDto();
            providerProfileDto.setId(providerProfile.getId());
            providerProfileDto.setCity(providerProfile.getCity());
            providerProfileDto.setStatus(providerProfile.getStatus());
            appUserDto.setProviderProfile(providerProfileDto);
        }

        return appUserDto;
    }

    @Override
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
