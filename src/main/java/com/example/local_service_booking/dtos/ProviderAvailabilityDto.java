package com.example.local_service_booking.dtos;

import com.example.local_service_booking.entities.DayOfWeekEnum;

import java.util.List;

public class ProviderAvailabilityDto {
    private Long id;
    private DayOfWeekEnum dayOfWeek;  // e.g., "Mon"
    private int startTime;     // 9 (9 AM)
    private int endTime;       // 18 (6 PM)
    private List<Integer> breaks; // optional, [13, 14] means 1-2 PM break

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeekEnum getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekEnum dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<Integer> breaks) {
        this.breaks = breaks;
    }
}

