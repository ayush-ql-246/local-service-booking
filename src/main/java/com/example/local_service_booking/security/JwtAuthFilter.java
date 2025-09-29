package com.example.local_service_booking.security;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.UserStatus;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.services.JwtService;
import com.example.local_service_booking.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtService.getJwtFromRequest(request);

        if (token != null) {
            try {
                String email = jwtService.extractEmail(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    AppUser user = userService.getUserByEmail(email);

                    if (user.getStatus().equals(UserStatus.BLOCKED)) {
                        handleUnauthorizedResponse(response, false, 2004, Constants.getMessage(2004), "Blocked User");
                        return;
                    }

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user, null, user.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (ExpiredJwtException e) {
                handleUnauthorizedResponse(response, false, 1028, Constants.getMessage(1028), "Please log in again");
                return;
            } catch (JwtException e) {
                handleUnauthorizedResponse(response, false, 2028, Constants.getMessage(2028), "Token is invalid");
                return;
            } catch (Exception e) {
                handleUnauthorizedResponse(response, false, 2038, Constants.getMessage(2038), "An error occurred");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleUnauthorizedResponse(HttpServletResponse response, boolean status, int code, String message, String error) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", status);
        responseBody.put("code", code);
        responseBody.put("message", message);
        responseBody.put("error", error);

        new ObjectMapper().writeValue(response.getWriter(), responseBody);
    }
}
