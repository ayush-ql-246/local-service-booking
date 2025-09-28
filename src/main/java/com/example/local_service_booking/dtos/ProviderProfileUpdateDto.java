package com.example.local_service_booking.dtos;

import com.example.local_service_booking.entities.ProviderStatus;

public class ProviderProfileUpdateDto {
    private String city;
    private ProviderStatus status;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ProviderStatus getStatus() {
        return status;
    }

    public void setStatus(ProviderStatus status) {
        this.status = status;
    }
}
