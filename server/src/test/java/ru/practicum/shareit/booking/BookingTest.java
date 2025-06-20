package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookingTest {
    @Test
    public void bookingGetSetTest() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(5);
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(startTime);
        booking.setEnd(endTime);
        booking.setItem(TestData.item1);
        booking.setBooker(TestData.requestor1);
        booking.setStatus(BookingStatus.WAITING);

        assertThat(booking.getId(), equalTo(1L));
        assertThat(booking.getStart(), equalTo(startTime));
        assertThat(booking.getEnd(), equalTo(endTime));
        assertThat(booking.getItem(), equalTo(TestData.item1));
        assertThat(booking.getBooker(), equalTo(TestData.requestor1));
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
    }
}
