package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingServiceImpl bookingService;

    @PostMapping()
    public BookingDto createBooking(
            @RequestHeader(SHARER_USER_ID) long bookerId,
            @RequestBody BookingCreateDto booking) {
        log.info("==> Create booking: {}", booking);
        BookingDto bookingDto = bookingService.createBooking(booking, bookerId);
        log.info("<== Created booking: {}", bookingDto);
        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(
            @PathVariable("bookingId") long bookingId,
            @RequestHeader(SHARER_USER_ID) long ownerId,
            @RequestParam(name = "approved") boolean approved) {
        log.info("==> Approve booking: {}", bookingId);
        BookingDto booking = bookingService.approveBooking(bookingId, ownerId, approved);
        log.info("<== Approved booking: {}", booking);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(
            @RequestHeader(SHARER_USER_ID) long userId,
            @PathVariable("bookingId") long bookingId) {
        log.info("==> Get booking: {}", bookingId);
        BookingDto booking = bookingService.getBookingById(userId, bookingId);
        log.info("<== Get the booking: {}", booking);
        return booking;
    }

    @GetMapping
    public List<BookingDto> getBookingsByBooker(
            @RequestHeader(SHARER_USER_ID) long bookerId,
            @RequestParam(name = "state") String state) {
        log.info("==> Get bookings by booker: {}", bookerId);
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new ValidationException("Unknown state: " + state));
        List<BookingDto> bookings = bookingService.getBookingsByBooker(bookerId, bookingState);
        log.info("<== Get all bookings by booker: {}", bookings);
        return bookings;
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(
            @RequestHeader(SHARER_USER_ID) long ownerId,
            @RequestParam(name = "state", defaultValue = "all") String state
    ) {
        log.info("==> Get bookings by owner: {}", ownerId);
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new ValidationException("Unknown state: " + state));
        List<BookingDto> bookings = bookingService.getBookingsByOwner(ownerId, bookingState);
        log.info("<== Get all bookings by owner: {}", bookings);
        return bookings;
    }
}
