package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.exception.ValidationException;

@Controller
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping()
    public ResponseEntity<Object> createBooking(
            @RequestHeader(SHARER_USER_ID) @NotNull @PositiveOrZero Long bookerId,
            @RequestBody @Valid BookingCreateDto booking) {
        log.info("==> Create booking: {}", booking);
        ResponseEntity<Object> bookingDto = bookingClient.createBooking(bookerId, booking);
        log.info("<== Created booking: {}", bookingDto);
        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(
            @PathVariable("bookingId") @NotNull @PositiveOrZero Long bookingId,
            @RequestHeader(SHARER_USER_ID) @NotNull @PositiveOrZero Long ownerId,
            @RequestParam(name = "approved") boolean approved) {
        log.info("==> Approve booking: {}", bookingId);
        ResponseEntity<Object> booking = bookingClient.approveBooking(bookingId, ownerId, approved);
        log.info("<== Approved booking: {}", booking);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(
            @RequestHeader(SHARER_USER_ID) @NotNull @PositiveOrZero Long userId,
            @PathVariable("bookingId") @NotNull @PositiveOrZero Long bookingId) {
        log.info("==> Get booking: {}, userId: {}", bookingId, userId);
        ResponseEntity<Object> booking = bookingClient.getBookingById(userId, bookingId);
        log.info("<== Get the booking: {}, userId: {}", booking, userId);
        return booking;
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(
            @RequestHeader(SHARER_USER_ID) @NotNull @PositiveOrZero Long bookerId,
            @RequestParam(name = "state", defaultValue = "all") String state) {
        log.info("==> Get bookings by booker: {}", bookerId);
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new ValidationException("Unknown state: " + state));
        ResponseEntity<Object> bookings = bookingClient.getBookingsByBooker(bookerId, bookingState);
        log.info("<== Get all bookings by booker: {}", bookings);
        return bookings;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(
            @RequestHeader(SHARER_USER_ID) @NotNull @PositiveOrZero Long ownerId,
            @RequestParam(name = "state", defaultValue = "all") String state
    ) {
        log.info("==> Get bookings by owner: {}", ownerId);
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new ValidationException("Unknown state: " + state));
        ResponseEntity<Object> bookings = bookingClient.getBookingsByOwner(ownerId, bookingState);
        log.info("<== Get all bookings by owner: {}", bookings);
        return bookings;
    }
}
