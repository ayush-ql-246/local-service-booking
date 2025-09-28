package com.example.local_service_booking.utils;

import com.example.local_service_booking.dtos.*;
import com.example.local_service_booking.entities.AppUser;
import com.example.local_service_booking.entities.Booking;
import com.example.local_service_booking.entities.ProviderService;
import com.example.local_service_booking.entities.Rating;

import java.util.ArrayList;
import java.util.List;

public class DtoUtils {

    public static List<ProviderServiceDto> mapProviderServicesToDto(List<ProviderService> services) {
        List<ProviderServiceDto> serviceDtos = new ArrayList<>();

        for (ProviderService service : services) {
            ProviderServiceDto serviceDto = new ProviderServiceDto();
            serviceDto.setId(service.getId());
            serviceDto.setBasePrice(service.getBasePrice());
            serviceDto.setCategory(service.getCategory());
            serviceDto.setDescription(service.getDescription());
            serviceDto.setIconUrl(service.getIconUrl());
            serviceDto.setPricingType(service.getPricingType());
            serviceDto.setCreatedAt(service.getCreatedAt());
            serviceDto.setUpdatedAt(service.getUpdatedAt());

            ProviderProfileDto providerProfileDto = getProviderProfileDto(service);

            serviceDto.setProvider(providerProfileDto);
            serviceDtos.add(serviceDto);
        }

        return serviceDtos;
    }

    private static ProviderProfileDto getProviderProfileDto(ProviderService service) {
        ProviderProfileDto providerProfileDto = new ProviderProfileDto();
        providerProfileDto.setId(service.getProvider().getId());
        providerProfileDto.setCity(service.getProvider().getCity());
        providerProfileDto.setStatus(service.getProvider().getStatus());
        providerProfileDto.setRatingAverage(service.getProvider().getRatingAverage());
        providerProfileDto.setCreatedAt(service.getCreatedAt());
        providerProfileDto.setUpdatedAt(service.getUpdatedAt());
        return providerProfileDto;
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookingStatus(booking.getStatus());
        bookingDto.setCreatedAt(booking.getCreatedAt());
        bookingDto.setUpdatedAt(booking.getCreatedAt());
        bookingDto.setStartTime(booking.getStartTime());
        bookingDto.setEndTime(booking.getEndTime());

        AppUserDto userDto = new AppUserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setEmail(booking.getUser().getEmail());
        userDto.setRole(booking.getUser().getRole());
        userDto.setAddress(booking.getUser().getAddress());
        userDto.setName(booking.getUser().getName());
        userDto.setPhoneNumber(booking.getUser().getPhoneNumber());
        bookingDto.setUser(userDto);

        ProviderProfileDto providerProfileDto = getProviderProfileDto(booking.getProvider());
        bookingDto.setProviderProfile(providerProfileDto);


        ProviderServiceDto providerServiceDto = getProviderServiceDto(booking.getService());
        bookingDto.setService(providerServiceDto);

        if(booking.getRating()!=null) {
            RatingDto ratingDto = getRatingDto(booking.getRating());
            bookingDto.setRating(ratingDto);
        }

        return bookingDto;
    }

    public static ProviderProfileDto getProviderProfileDto(AppUser provider) {
        ProviderProfileDto providerProfileDto = new ProviderProfileDto();
        providerProfileDto.setId(provider.getProviderProfile().getId());
        providerProfileDto.setStatus(provider.getProviderProfile().getStatus());
        providerProfileDto.setRatingAverage(provider.getProviderProfile().getRatingAverage());
        providerProfileDto.setCity(provider.getProviderProfile().getCity());
        providerProfileDto.setCreatedAt(provider.getProviderProfile().getCreatedAt());
        providerProfileDto.setUpdatedAt(provider.getProviderProfile().getUpdatedAt());
        return providerProfileDto;
    }

    public static ProviderServiceDto getProviderServiceDto(ProviderService providerService) {
        ProviderServiceDto providerServiceDto = new ProviderServiceDto();
        providerServiceDto.setId(providerService.getId());
        providerServiceDto.setDescription(providerService.getDescription());
        providerServiceDto.setCategory(providerService.getCategory());
        providerServiceDto.setPricingType(providerService.getPricingType());
        providerServiceDto.setBasePrice(providerService.getBasePrice());
        providerServiceDto.setIconUrl(providerService.getIconUrl());
        providerServiceDto.setCreatedAt(providerService.getCreatedAt());
        providerServiceDto.setUpdatedAt(providerService.getUpdatedAt());
        return providerServiceDto;
    }

    public static RatingDto getRatingDto(Rating rating) {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setId(rating.getId());
        ratingDto.setRating(rating.getRating());
        ratingDto.setComment(rating.getComment());
        ratingDto.setUserId(rating.getUser().getId());
        ratingDto.setBookingId(rating.getBooking().getId());
        ratingDto.setProviderId(rating.getProvider().getId());
        ratingDto.setUpdatedAt(rating.getUpdatedAt());
        ratingDto.setCreatedAt(rating.getCreatedAt());

        return ratingDto;
    }
}
