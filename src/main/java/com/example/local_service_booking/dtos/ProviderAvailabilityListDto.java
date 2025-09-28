package com.example.local_service_booking.dtos;

import java.util.List;

public class ProviderAvailabilityListDto {
    List<ProviderAvailabilityDto> availabilityDtos;

    public List<ProviderAvailabilityDto> getAvailabilityDtos() {
        return availabilityDtos;
    }

    public void setAvailabilityDtos(List<ProviderAvailabilityDto> availabilityDtos) {
        this.availabilityDtos = availabilityDtos;
    }
}
