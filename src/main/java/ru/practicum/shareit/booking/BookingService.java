package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingCreateDto bookingDto, long bookerId);

    BookingDto approveBooking(long bookingId, long ownerId, boolean approved);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getBookingsByBooker(long bookerId, BookingState bookingState);

    List<BookingDto> getBookingsByOwner(long ownerId, BookingState bookingState);
}
