package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Component
@Validated
public class BookingMapper {
    public Booking toModel(BookingCreateDto bookingDto,
                           User booker, Item item) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        return booking;
    }

    public BookingDto toDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());

        Item item = booking.getItem();
        bookingDto.setItem(new BookingDto.ItemDto(item.getId(), item.getName()));

        User booker = booking.getBooker();
        bookingDto.setBooker(new BookingDto.UserDto(booker.getId(), booker.getName()));
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

}
