package ru.practicum.shareit.exception;

public class NotFoundBookingException extends RuntimeException {
    public NotFoundBookingException(long bookingId) {
        super("Booking not found with id " + bookingId);
    }
}
