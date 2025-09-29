package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.dtos.AppUserDto;
import com.example.local_service_booking.dtos.BookingDto;
import com.example.local_service_booking.dtos.BookingRequestDto;
import com.example.local_service_booking.entities.*;
import com.example.local_service_booking.exceptions.*;
import com.example.local_service_booking.repositories.AppUserRepository;
import com.example.local_service_booking.repositories.BookingRepository;
import com.example.local_service_booking.repositories.ProviderServiceRepository;
import com.example.local_service_booking.services.BookingService;
import com.example.local_service_booking.utils.DtoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingServiceImpl implements BookingService {

    private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final AppUserRepository userRepository;
    private final ProviderServiceRepository providerServiceRepository;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(AppUserRepository userRepository,
                              ProviderServiceRepository providerServiceRepository,
                              BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.providerServiceRepository = providerServiceRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDto createBooking(BookingRequestDto request) throws Exception {
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new InvalidServiceRequestException("startTime and endTime are required");
        }
        if (request.getStartTime() < 0 || request.getStartTime() > 23 ||
                request.getEndTime() < 0 || request.getEndTime() > 23 ||
                request.getStartTime() >= request.getEndTime()) {
            throw new InvalidServiceRequestException("Invalid startTime/endTime. Use 0..23 and startTime < endTime");
        }

        AppUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ProviderService providerService = providerServiceRepository.findById(request.getProviderServiceId())
                .orElseThrow(() -> new ServiceNotFoundException("Service not found"));


        // Validate providerId matches the providerService's provider
        Long requestedProviderId = request.getProviderId();
        if (requestedProviderId == null) {
            throw new InvalidServiceRequestException("providerId is required");
        }
        Long actualProviderProfileId = providerService.getProvider().getId();
        if (!requestedProviderId.equals(actualProviderProfileId)) {
            throw new InvalidServiceRequestException("providerId does not match the selected provider service");
        }

        List<Booking> overlappingProviderBookings = bookingRepository.findOverlappingBookingsForProvider(
                request.getProviderId(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (!overlappingProviderBookings.isEmpty()) {
            throw new InvalidServiceRequestException("Provider is already booked during this time slot.");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setProvider(providerService.getProvider().getUser());
        booking.setService(providerService);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setStatus(BookingStatus.REQUESTED);
        booking.setCreatedAt(System.currentTimeMillis());
        booking.setUpdatedAt(System.currentTimeMillis());

        bookingRepository.save(booking);

        BookingDto bookingDto = DtoUtils.mapToBookingDto(booking);

        return bookingDto;
    }

    @Override
    public Map<String, Object> getBookingsByUser(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Booking> bookingsPage = bookingRepository.findByUserId(userId, pageable);

        List<BookingDto> bookings = bookingsPage.stream()
                .map(DtoUtils::mapToBookingDto)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("bookings", bookings);
        response.put("currentPage", bookingsPage.getNumber());
        response.put("totalItems", bookingsPage.getTotalElements());
        response.put("totalPages", bookingsPage.getTotalPages());

        return response;
    }

    @Override
    public Map<String, Object> getBookingsByProvider(Long providerId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Booking> bookingsPage = bookingRepository.findByProviderId(providerId, pageable);

        List<BookingDto> bookings = bookingsPage.getContent()
                .stream()
                .map(DtoUtils::mapToBookingDto)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("bookings", bookings);
        response.put("currentPage", bookingsPage.getNumber());
        response.put("totalItems", bookingsPage.getTotalElements());
        response.put("totalPages", bookingsPage.getTotalPages());

        return response;
    }

    @Override
    public BookingDto getBookingById(Long bookingId, AppUserDto userDto) throws Exception {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Ensure only the user or provider involved can see it
        if (!booking.getUser().getId().equals(userDto.getId()) && !booking.getProvider().getId().equals(userDto.getId())) {
            throw new BookingException("You can only view your own bookings");
        }

        return DtoUtils.mapToBookingDto(booking);
    }


    @Override
    public BookingDto updateBookingStatus(Long providerId, Long bookingId, BookingStatus status) throws Exception {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Validate provider owns the booking
        if (!booking.getProvider().getId().equals(providerId)) {
            throw new UnauthorizedAccessException("Unauthorized: You can only update your own bookings");
        }

        if(booking.getStatus().equals(BookingStatus.COMPLETED)) {
            throw new InvalidServiceRequestException("This booking is already completed");
        }

        // Update status
        booking.setStatus(status);
        booking.setUpdatedAt(System.currentTimeMillis());
        bookingRepository.save(booking);

        if(booking.getStatus().equals(BookingStatus.COMPLETED)) {
            if(booking.getService().getPricingType().equals(PricingType.FIXED)) {
                log.info("Price to be paid : {}", booking.getService().getBasePrice());
                // Add payment integration

            } else {
                Long workedTimeMillis = booking.getEndTime() - booking.getStartTime();

                // Convert milliseconds to hours (round up to next hour if there is any extra time)
                double hoursWorked = Math.ceil(workedTimeMillis / (1000.0 * 60 * 60));
                double totalPrice = hoursWorked * booking.getService().getBasePrice();
                log.info("Price to be paid (Hourly): {} for {} hours", totalPrice, hoursWorked);
                // Add payment integration

            }

        }

        // Send notification to user
        String message = "Your booking with ID " + bookingId + " has been " + status.name();
        log.info("User Notification: {}", message);

        return DtoUtils.mapToBookingDto(booking);
    }

    @Override
    public BookingDto cancelBooking(Long bookingId, Long userId) throws Exception {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingException("Booking not found"));

        // Validate that this booking belongs to the user
        if (!booking.getUser().getId().equals(userId)) {
            throw new BookingException("You can only cancel your own bookings");
        }

        // Check if booking start time is in future
        if (System.currentTimeMillis() > booking.getStartTime()) {
            throw new BookingException("Cannot cancel booking after it has started");
        }

        // Update status
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(System.currentTimeMillis());
        bookingRepository.save(booking);

        // Notify provider
        String message = "Booking ID " + bookingId + " was cancelled by user.";
        log.info("Provider Notification: {}", message);

        return DtoUtils.mapToBookingDto(booking);
    }

    @Override
    public Page<BookingDto> getAllBookings(BookingStatus status, Long providerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Booking> bookings;
        if (status != null && providerId != null) {
            bookings = bookingRepository.findByStatusAndProviderId(status, providerId, pageable);
        } else if (status != null) {
            bookings = bookingRepository.findByStatus(status, pageable);
        } else if (providerId != null) {
            bookings = bookingRepository.findByProviderId(providerId, pageable);
        } else {
            bookings = bookingRepository.findAll(pageable);
        }

        return bookings.map(DtoUtils::mapToBookingDto);
    }
}
