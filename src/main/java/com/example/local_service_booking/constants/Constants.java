package com.example.local_service_booking.constants;

import java.util.Map;

public class Constants {

    public Constants() { }

    public static final Map<Integer, String> statusMap = Map.ofEntries(

        // Response messages
        Map.entry(1001, "The user was successfully created"),
        Map.entry(1002, "OTP sent successfully"),
        Map.entry(1003, "Invalid or expired OTP"),
        Map.entry(1004, "Login successful"),
        Map.entry(1005, "Booking created successfully"),
        Map.entry(1006, "User bookings fetched successfully"),
        Map.entry(1007, "Provider bookings fetched successfully"),
        Map.entry(1008, "Booking details fetched successfully"),
        Map.entry(1009, "Booking status updated successfully"),
        Map.entry(1010, "Booking cancelled successfully"),
        Map.entry(1011, "Rating added successfully"),
        Map.entry(1012, "Service added/updated successfully"),
        Map.entry(1013, "Services fetched successfully"),
        Map.entry(1014, "Service deleted successfully"),
        Map.entry(1015, "Availability updated successfully"),
        Map.entry(1016, "Availability fetched successfully"),
        Map.entry(1017, "Provider profile updated successfully"),
        Map.entry(1018, "Available services fetched successfully"),
        Map.entry(1019, "User fetched successfully"),
        Map.entry(1020, "User profile updated successfully"),
        Map.entry(1021, "User Already Exists"),
        Map.entry(1022, "Wrong data provided"),
        Map.entry(1023, "Booking related error occurred"),
        Map.entry(1024, "User validation error occurred"),
        Map.entry(1025, "Users fetched successfully"),
        Map.entry(1026, "User status updated successfully"),
        Map.entry(1027, "Bookings fetched successfully"),
        Map.entry(1028, "Token expired"),
        Map.entry(401, "Unauthorized access"),
        Map.entry(500, "Internal Server Error"),

        // Exception messages
        Map.entry(2001, "Hash token not generated"),
        Map.entry(2002, "Email or phone number required"),
        Map.entry(2003, "User not exist"),
        Map.entry(2004, "This account is blocked."),
        Map.entry(2005, "OTP verification failed"),
        Map.entry(2006, "User can create booking only for their accounts"),
        Map.entry(2007, "User not found"),
        Map.entry(2008, "Profile not found"),
        Map.entry(2009, "Phone number should be of 10 characters"),
        Map.entry(2010, "Cannot change status of admin accounts"),
        Map.entry(2011, "Cannot create admin accounts via registration"),
        Map.entry(2012, "Email already in use"),
        Map.entry(2013, "Phone number already in use"),
        Map.entry(2014, "Email or phone number required"),
        Map.entry(2015, "startTime and endTime are required"),
        Map.entry(2016, "Invalid startTime/endTime. Use 0..23 and startTime < endTime"),
        Map.entry(2017, "Service not found"),
        Map.entry(2018, "providerId is required"),
        Map.entry(2019, "providerId does not match the selected provider service"),
        Map.entry(2020, "Provider is already booked during this time slot."),
        Map.entry(2021, "Booking not found"),
        Map.entry(2022, "You can only view your own bookings"),
        Map.entry(2023, "Unauthorized: You can only update your own bookings"),
        Map.entry(2024, "This booking is already completed"),
        Map.entry(2025, "You can only cancel your own bookings"),
        Map.entry(2026, "Cannot cancel booking after it has started"),
        Map.entry(2027, "User token invalid"),
        Map.entry(2028, "Invalid token"),
        Map.entry(2029, "You are not allowed to delete this service"),
        Map.entry(2030, "startTime and endTime should be in the range 0-23"),
        Map.entry(2031, "endTime should be greater than startTime"),
        Map.entry(2032, "Rating should be in the range of 1-5"),
        Map.entry(2033, "Rating can only be added after booking is completed"),
        Map.entry(2034, "Rating already submitted for this booking"),
        Map.entry(2035, "You are not allowed to rate this booking"),
        Map.entry(2036, "Provider not found"),
        Map.entry(2037, "Provider profile not found"),
        Map.entry(2038, "Invalid Request")
    );

    public static String getMessage(Integer statusCode) {
        return statusMap.get(statusCode);
    }
}
