package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private BookingMapper bookingMapper;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createBooking() throws Exception {
        BookingDto booking = bookingMapper.toDto(TestData.booking1);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(TestData.item1.getId(),
                LocalDateTime.parse("2024-02-01T09:00:00"), LocalDateTime.parse("2024-02-01T17:00:00"));
        when(bookingService.createBooking(bookingCreateDto, TestData.requestor1.getId())).thenReturn(booking);
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(bookingCreateDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", TestData.requestor1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(TestData.booking1.getStatus().name())));
        verify(bookingService, times(1)).createBooking(bookingCreateDto, TestData.requestor1.getId());
    }

    @Test
    void approveBooking() throws Exception {
        BookingDto booking = bookingMapper.toDto(TestData.booking1);
        when(bookingService.approveBooking(TestData.booking1.getId(), TestData.requestor1.getId(), true))
                .thenReturn(booking);

        mockMvc.perform(patch("/bookings/" + booking.getId())
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", TestData.requestor1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(TestData.booking1.getStatus().name())));
        verify(bookingService, times(1)).approveBooking(booking.getId(), TestData.requestor1.getId(), true);
    }

    @Test
    void getBookingById() throws Exception {
        BookingDto booking = bookingMapper.toDto(TestData.booking1);
        when(bookingService.getBookingById(TestData.requestor1.getId(), booking.getId())).thenReturn(booking);
        mockMvc.perform(get("/bookings/" + TestData.booking1.getId())
                        .header("X-Sharer-User-Id", TestData.requestor1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(TestData.booking1.getStatus().name())));
        verify(bookingService, times(1)).getBookingById(TestData.requestor1.getId(), booking.getId());
    }

    @Test
    void getBookingsByBooker_Success() throws Exception {
        List<BookingDto> bookings = List.of(
                bookingMapper.toDto(TestData.booking1),
                bookingMapper.toDto(TestData.booking3)
        );
        when(bookingService.getBookingsByBooker(TestData.requestor1.getId(), BookingState.ALL)).thenReturn(bookings);
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", TestData.requestor1.getId())
                        .param("state", BookingState.ALL.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(bookings.size())));
        verify(bookingService, times(1)).getBookingsByBooker(TestData.requestor1.getId(), BookingState.ALL);
    }

    @Test
    void getBookingsByBookerWithNotValidState_ValidationException() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", TestData.requestor1.getId())
                        .param("state", "Not Valid State"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingService);
    }

    @Test
    void getBookingsByOwner_Success() throws Exception {
        List<BookingDto> bookings = List.of(
                bookingMapper.toDto(TestData.booking1),
                bookingMapper.toDto(TestData.booking2),
                bookingMapper.toDto(TestData.booking3)
        );
        when(bookingService.getBookingsByOwner(TestData.owner.getId(), BookingState.ALL)).thenReturn(bookings);
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", TestData.owner.getId())
                        .param("state", BookingState.ALL.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(bookings.size())));
        verify(bookingService, times(1)).getBookingsByOwner(TestData.owner.getId(), BookingState.ALL);
    }

    @Test
    void getBookingsByOwnerWithNotValidState_ValidationException() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", TestData.owner.getId())
                        .param("state", "Not Valid State"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(bookingService);
    }
}
