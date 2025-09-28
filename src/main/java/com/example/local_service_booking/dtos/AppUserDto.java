package com.example.local_service_booking.dtos;

import com.example.local_service_booking.entities.UserRoles;

public class AppUserDto {
    private Long id;
    private String email;
    private String phoneNumber;
    private String name;
    private String address;
    private UserRoles role;
    private ProviderProfileDto providerProfile;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public ProviderProfileDto getProviderProfile() {
        return providerProfile;
    }

    public void setProviderProfile(ProviderProfileDto providerProfile) {
        this.providerProfile = providerProfile;
    }
}
