package com.example.local_service_booking.converters;

import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceTypesConverter implements AttributeConverter<List<Long>, String> {
    private static final String SEPARATOR = "$";

    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        // Add $ at start and end
        return SEPARATOR + attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(SEPARATOR)) + SEPARATOR;
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return List.of();
        }
        // Remove leading/trailing $ before splitting
        String trimmed = dbData.substring(1, dbData.length() - 1);
        return Arrays.stream(trimmed.split("\\" + SEPARATOR))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}
