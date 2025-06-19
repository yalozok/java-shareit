package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    private static final Sort SORT_NEW_OLD = Sort.by(Sort.Direction.DESC, "start");
    LocalDateTime now = LocalDateTime.now();

    @Test
    void findAllByBookerId_ListBookings() {
        List<Booking> bookings = bookingRepository.findAllByBooker_Id(TestData.requestor1.getId(), SORT_NEW_OLD);
        assertThat(bookings, hasSize(2));
        assertThat(bookings.getFirst().getStart().isAfter(bookings.getLast().getEnd()), is(true));
    }

    @Test
    void findAllByItemOwner_ListBookings() {
        List<Booking> bookings = bookingRepository.findAllByOwner_Id(TestData.owner.getId());
        assertThat(bookings, hasSize(3));
        assertThat(bookings.getFirst().getStart().isAfter(bookings.get(1).getEnd()), is(true));
    }

    @Test
    void findCurrentByBookerId_ListBookings() {
        List<Booking> bookings = bookingRepository
                .findCurrentByBookerId(TestData.requestor1.getId(), now, now);
        assertThat(bookings, hasSize(0));
    }

    @Test
    void findCurrentByOwnerId_ListBookings() {
        List<Booking> bookings = bookingRepository
                .findCurrentByOwnerId(TestData.owner.getId(), now, now);
        assertThat(bookings, hasSize(0));
    }

    @Test
    void findPastByBookerId_ListBookings() {
        List<Booking> bookings = bookingRepository
                .findPastByBookerId(TestData.requestor1.getId(), now);
        assertThat(bookings, hasSize(2));
    }

    @Test
    void findPastByOwnerId_ListBookings() {
        List<Booking> bookings = bookingRepository
                .findPastByOwnerId(TestData.owner.getId(), now);
        assertThat(bookings, hasSize(3));
    }

    @Test
    void findFutureByBookerId_ListBookings() {
        List<Booking> bookings = bookingRepository
                .findFutureByBookerId(TestData.requestor1.getId(), now);
        assertThat(bookings, hasSize(0));
    }

    @Test
    void findFutureByOwnerId_ListBookings() {
        List<Booking> bookings = bookingRepository
                .findFutureByItemOwnerId(TestData.owner.getId(), now);
        assertThat(bookings, hasSize(0));
    }

    @Test
    void findAllByBookerIdAndStatus_ListBookings() {
        List<Booking> bookings = bookingRepository
                .findAllByBooker_IdAndStatus(TestData.requestor1.getId(), BookingStatus.APPROVED, SORT_NEW_OLD);
        assertThat(bookings, hasSize(1));
        assertThat(bookings.getFirst().getId(), equalTo(TestData.booking1.getId()));
    }

    @Test
    void findAllByOwnerIdAndStatus_ListBookings() {
        List<Booking> bookings = bookingRepository
                .findAllByOwnerIdAndStatus(TestData.owner.getId(), BookingStatus.WAITING);
        assertThat(bookings, hasSize(1));
        assertThat(bookings.getFirst().getId(), equalTo(TestData.booking2.getId()));
    }

    @Test
    void findLastBooking_Booking() {
        Optional<Booking> booking = bookingRepository
                .findLastBooking(TestData.item1.getId(), now);
        assertThat(booking.isPresent(), is(true));
        assertThat(booking.get().getId(), equalTo(TestData.booking1.getId()));
    }

    @Test
    void findNextBooking_Booking() {
        Optional<Booking> booking = bookingRepository
                .findNextBooking(TestData.item1.getId(), now);
        assertThat(booking.isPresent(), is(false));
    }

    @Test
    void findByItemIdAndBookerId_BookingExist() {
        Optional<Booking> booking = bookingRepository
                .findByItem_IdAndBooker_Id(TestData.item1.getId(), TestData.requestor1.getId());
        assertThat(booking.isPresent(), is(true));
    }

    @Test
    void findByItemIdAndBookerId_BookingNotExist() {
        Optional<Booking> booking = bookingRepository
                .findByItem_IdAndBooker_Id(TestData.item1.getId(), TestData.requestor2.getId());
        assertThat(booking.isPresent(), is(false));
    }

    @Test
    void deleteByItem_BookingDeleted() {
        List<Booking> bookingsBeforeDeleting = bookingRepository.findAllByOwner_Id(TestData.owner.getId());
        assertThat(bookingsBeforeDeleting, hasSize(3));

        bookingRepository.deleteByItem(TestData.item1);

        List<Booking> bookingsAfterDeleting = bookingRepository.findAllByOwner_Id(TestData.owner.getId());
        assertThat(bookingsAfterDeleting, hasSize(2));
    }

}
