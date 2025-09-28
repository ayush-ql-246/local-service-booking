package com.example.local_service_booking.dtos;

import com.example.local_service_booking.entities.UserRoles;

public class TokenDto {
    private Long id;
    private String email;
    private UserRoles role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}
