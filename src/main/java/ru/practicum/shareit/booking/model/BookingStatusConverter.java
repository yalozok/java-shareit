package ru.practicum.shareit.booking.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookingStatusConverter implements AttributeConverter<BookingStatus, String> {
    @Override
    public String convertToDatabaseColumn(BookingStatus status) {
        return status == null ? null : status.name().toLowerCase();
    }

    @Override
    public BookingStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : BookingStatus.valueOf(dbData.toUpperCase());
    }
}