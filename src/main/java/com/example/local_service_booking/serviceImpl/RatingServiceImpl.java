package com.example.local_service_booking.serviceImpl;

import com.example.local_service_booking.constants.Constants;
import com.example.local_service_booking.dtos.RatingDto;
import com.example.local_service_booking.dtos.RatingRequestDto;
import com.example.local_service_booking.entities.*;
import com.example.local_service_booking.exceptions.InvalidServiceRequestException;
import com.example.local_service_booking.repositories.BookingRepository;
import com.example.local_service_booking.repositories.RatingRepository;
import com.example.local_service_booking.services.RatingService;
import com.example.local_service_booking.utils.DtoUtils;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    private final BookingRepository bookingRepository;
    private final RatingRepository ratingRepository;

    public RatingServiceImpl(BookingRepository bookingRepository, RatingRepository ratingRepository) {
        this.bookingRepository = bookingRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public RatingDto addRating(Long bookingId, RatingRequestDto request, Long userId) throws Exception {

        if(request.getRating()<1 || request.getRating()>5) {
            throw new InvalidServiceRequestException(Constants.getMessage(2032));
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new InvalidServiceRequestException(Constants.getMessage(2021)));

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new InvalidServiceRequestException(Constants.getMessage(2033));
        }

        if (ratingRepository.findByBookingId(bookingId).isPresent()) {
            throw new InvalidServiceRequestException(Constants.getMessage(2034));
        }

        AppUser user = booking.getUser();
        AppUser provider = booking.getProvider();

        if (!user.getId().equals(userId)) {
            throw new RuntimeException(Constants.getMessage(2035));
        }

        Rating rating = new Rating();
        rating.setBooking(booking);
        rating.setUser(user);
        rating.setProvider(provider);
        rating.setRating(request.getRating());
        rating.setComment(request.getComment());
        rating.setCreatedAt(System.currentTimeMillis());
        rating.setUpdatedAt(System.currentTimeMillis());
        ratingRepository.save(rating);

        // Update average rating of provider
        ProviderProfile providerProfile = provider.getProviderProfile();
        Double avgRating = ratingRepository.getAverageRatingByProvider(provider.getId());
        providerProfile.setRatingAverage(avgRating);

        return DtoUtils.getRatingDto(rating);
    }
}
