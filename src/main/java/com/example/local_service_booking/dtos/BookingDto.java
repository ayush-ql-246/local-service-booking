package com.example.local_service_booking.dtos;

import com.example.local_service_booking.entities.BookingStatus;

public class BookingDto {
    private Long id;
    private AppUserDto user;
    private ProviderProfileDto providerProfile;
    private ProviderServiceDto service;
    private Long startTime;
    private Long endTime;
    private BookingStatus bookingStatus;
    private RatingDto rating;
    private Long createdAt;
    private Long updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUserDto getUser() {
        return user;
    }

    public void setUser(AppUserDto user) {
        this.user = user;
    }

    public ProviderProfileDto getProviderProfile() {
        return providerProfile;
    }

    public void setProviderProfile(ProviderProfileDto providerProfile) {
        this.providerProfile = providerProfile;
    }

    public ProviderServiceDto getService() {
        return service;
    }

    public void setService(ProviderServiceDto service) {
        this.service = service;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public RatingDto getRating() {
        return rating;
    }

    public void setRating(RatingDto rating) {
        this.rating = rating;
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
