package com.example.local_service_booking.dtos;

import com.example.local_service_booking.entities.BookingStatus;

public class BookingStatusUpdateDto {
    private BookingStatus status;

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
