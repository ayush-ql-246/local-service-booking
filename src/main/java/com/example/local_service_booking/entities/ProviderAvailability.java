package com.example.local_service_booking.entities;

import com.example.local_service_booking.converters.IntListConverter;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "provider_availability")
public class ProviderAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ProviderProfile provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeekEnum dayOfWeek;

    @Column(name = "start_time")
    private Integer startTime;  // 0-23

    @Column(name = "end_time")
    private Integer endTime;    // 0-23

    @Convert(converter = IntListConverter.class)
    @Column(name = "breaks")
    private List<Integer> breaks; // 0-23 integers

    @Column(name = "created_at", updatable = false)
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @PrePersist
    public void prePersist() {
        long now = System.currentTimeMillis();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProviderProfile getProvider() {
        return provider;
    }

    public void setProvider(ProviderProfile provider) {
        this.provider = provider;
    }

    public DayOfWeekEnum getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekEnum dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<Integer> breaks) {
        this.breaks = breaks;
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
