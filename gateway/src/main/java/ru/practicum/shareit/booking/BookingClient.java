package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long bookerId, BookingCreateDto bookingDto) {
        return post("", bookerId, bookingDto);
    }

    public ResponseEntity<Object> approveBooking(long bookingId, long ownerId, boolean approved) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId,
                "approved", approved
        );
        return patch("/{bookingId}?approved={approved}", ownerId, parameters, null);
    }

    public ResponseEntity<Object> getBookingById(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsByBooker(long userId, BookingState state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwner(long ownerId, BookingState state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("/owner?state={state}", ownerId, parameters);
    }
}
