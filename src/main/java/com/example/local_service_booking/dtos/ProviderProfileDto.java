package com.example.local_service_booking.dtos;

import com.example.local_service_booking.entities.ProviderStatus;

import java.util.List;

public class ProviderProfileDto {
    private Long id;
    List<ProviderServiceDto> providerServices;
    private String city;
    private ProviderStatus status;
    private Double ratingAverage;
    private Long createdAt;
    private Long updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProviderServiceDto> getProviderServices() {
        return providerServices;
    }

    public void setProviderServices(List<ProviderServiceDto> providerServices) {
        this.providerServices = providerServices;
    }

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

    public Double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
