package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.TestDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundBookingException;
import ru.practicum.shareit.exception.NotFoundItemException;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class BookingServiceImplTest {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final EntityManager em;

    @Test
    void createBookingByOwner_ForbiddenOperationException() {
        ForbiddenOperationException exception = assertThrows(
                ForbiddenOperationException.class,
                () -> bookingService.createBooking(TestDto.bookingCreateDto, TestData.owner.getId())
        );
        assertThat(exception.getMessage(), equalTo("Forbidden operation for this user: " +
                TestData.owner.getId()));
    }

    @Test
    void createBookingNotAvailableItem_NotAvailableException() {
        NotAvailableItemException exception = assertThrows(
                NotAvailableItemException.class,
                () -> bookingService.createBooking(TestDto.bookingCreateDtoNotAvailable,
                        TestData.requestor1.getId())
        );
        assertThat(exception.getMessage(), equalTo("Item with id " + TestData.item3.getId() + " is not available"));
    }

    @Test
    void createBookingNotExistingItem_NotFoundItemException() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto(Long.MAX_VALUE,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5));

        NotFoundItemException exception = assertThrows(
                NotFoundItemException.class,
                () -> bookingService.createBooking(bookingCreateDto, TestData.owner.getId())
        );
        assertThat(exception.getMessage(), equalTo("Item not found: " + Long.MAX_VALUE));
    }

    @Test
    void approveBooking_BookingApproved() {
        BookingDto booking = bookingService.approveBooking(TestData.booking2.getId(), TestData.owner.getId(), true);
        assertThat(booking.getStatus(), equalTo(BookingStatus.APPROVED));

        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class);
        query.setParameter("bookingId", booking.getId());
        Booking bookingFromDB = query.getSingleResult();
        BookingDto expected = bookingMapper.toDto(bookingFromDB);
        assertThat(booking.getStatus(), equalTo(expected.getStatus()));
    }

    @Test
    void approveBooking_BookingRejected() {
        BookingDto booking = bookingService.approveBooking(TestData.booking2.getId(), TestData.owner.getId(), false);
        assertThat(booking.getStatus(), equalTo(BookingStatus.REJECTED));

        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class);
        query.setParameter("bookingId", booking.getId());
        Booking bookingFromDB = query.getSingleResult();
        BookingDto expected = bookingMapper.toDto(bookingFromDB);
        assertThat(booking.getStatus(), equalTo(expected.getStatus()));
    }

    @Test
    void approveNotExistingBooking_NotFoundBookingException() {
        NotFoundBookingException exception = assertThrows(
                NotFoundBookingException.class,
                () -> bookingService.approveBooking(Long.MAX_VALUE, TestData.owner.getId(), true)
        );
        assertThat(exception.getMessage(), equalTo("Booking not found with id " + Long.MAX_VALUE));
    }

    @Test
    void getBookingByIdByOwner_BookingFound() {
        BookingDto booking = bookingService.getBookingById(TestData.owner.getId(), TestData.booking1.getId());
        assertThat(booking.getId(), equalTo(TestData.booking1.getId()));
    }

    @Test
    void getBookingByIdByBooker_BookingFound() {
        BookingDto booking = bookingService.getBookingById(TestData.requestor1.getId(), TestData.booking1.getId());
        assertThat(booking.getId(), equalTo(TestData.booking1.getId()));
    }

    @Test
    void getBookingByIdByNotOwnerNotBooker_ForbiddenOperationException() {
        ForbiddenOperationException exception = assertThrows(
                ForbiddenOperationException.class,
                () -> bookingService.getBookingById(TestData.requestor2.getId(), TestData.booking1.getId())
        );
        assertThat(exception.getMessage(), equalTo("Forbidden operation for this user: " +
                TestData.requestor2.getId()));
    }

    @Test
    void getAllBookingsByBooker_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByBooker(TestData.requestor1.getId(), BookingState.ALL);
        assertThat(bookings.size(), equalTo(2));
    }

    @Test
    void getPastBookingsByBooker_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByBooker(TestData.requestor1.getId(), BookingState.PAST);
        assertThat(bookings.size(), equalTo(2));
    }

    @Test
    void getFutureBookingsByBooker_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByBooker(TestData.requestor1.getId(), BookingState.FUTURE);
        assertThat(bookings, is(empty()));
    }

    @Test
    void getCurrentBookingsByBooker_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByBooker(TestData.requestor1.getId(), BookingState.CURRENT);
        assertThat(bookings, is(empty()));
    }

    @Test
    void getWaitingBookingsByBooker_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByBooker(TestData.requestor1.getId(), BookingState.WAITING);
        assertThat(bookings, is(empty()));
    }

    @Test
    void getRejectedBookingsByBooker_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByBooker(TestData.requestor1.getId(), BookingState.REJECTED);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    void getAllBookingsByOwner_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByOwner(TestData.owner.getId(), BookingState.ALL);
        assertThat(bookings.size(), equalTo(3));
    }

    @Test
    void getRejectedBookingsByOwner_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByOwner(TestData.owner.getId(), BookingState.REJECTED);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    void getWaitingBookingsByOwner_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByOwner(TestData.owner.getId(), BookingState.WAITING);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    void getPastBookingsByOwner_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByOwner(TestData.owner.getId(), BookingState.PAST);
        assertThat(bookings.size(), equalTo(3));
    }

    @Test
    void getCurrentBookingsByOwner_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByOwner(TestData.owner.getId(), BookingState.CURRENT);
        assertThat(bookings, is(empty()));
    }

    @Test
    void getFutureBookingsByOwner_ListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByOwner(TestData.owner.getId(), BookingState.FUTURE);
        assertThat(bookings, is(empty()));
    }

    @Test
    void getBookingsByUserWithoutItems_EmptyListBookings() {
        List<BookingDto> bookings = bookingService.getBookingsByOwner(TestData.requestor1.getId(), BookingState.ALL);
        assertThat(bookings, is(empty()));
    }

    @Test
    void toModel_ShouldMapFieldsCorrectly() {
        Booking booking = bookingMapper.toModel(TestDto.bookingCreateDto, TestData.requestor1, TestData.item1);
        assertThat(booking.getStart(), equalTo(TestDto.bookingCreateDto.getStart()));
        assertThat(booking.getEnd(), equalTo(TestDto.bookingCreateDto.getEnd()));
        assertThat(booking.getItem(), equalTo(TestData.item1));
        assertThat(booking.getBooker(), equalTo(TestData.requestor1));
    }

}
