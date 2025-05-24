package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
public class Booking {
    long id;
    LocalDateTime start;
    LocalDateTime end;
    long item;
    long booker;
    BookingStatus status;

}
