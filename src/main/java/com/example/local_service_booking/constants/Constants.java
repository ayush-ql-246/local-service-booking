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
        Map.entry(401, "Unauthorized access"),
        Map.entry(500, "Internal Server Error"),

        // Exception messages
        Map.entry(2001, "Hash token not generated"),
        Map.entry(2002, "Email or phone number required"),
        Map.entry(2003, "User not exist"),
        Map.entry(2004, "This account is blocked.")
    );

    public static String getMessage(Integer statusCode) {
        return statusMap.get(statusCode);
    }
}
