package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundBookingException;
import ru.practicum.shareit.exception.NotFoundItemException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private static final Sort SORT_NEW_OLD = Sort.by(Sort.Direction.DESC, "start");
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto createBooking(BookingCreateDto bookingDto, long bookerId) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundUserException(bookerId));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundItemException(bookingDto.getItemId()));

        if (item.getOwner().getId() == bookerId) {
            throw new ForbiddenOperationException(bookerId);
        }
        if (!item.isAvailable()) {
            throw new NotAvailableItemException(item.getId());
        }
        Booking booking = bookingMapper.toModel(bookingDto, booker, item);
        booking.setStatus(BookingStatus.WAITING);
        Booking bookingSaved = bookingRepository.save(booking);
        return bookingMapper.toDto(bookingSaved);
    }

    @Override
    public BookingDto approveBooking(long bookingId, long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundBookingException(bookingId));
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new ForbiddenOperationException(ownerId);
        }
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundUserException(ownerId));

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        Booking bookingSaved = bookingRepository.save(booking);
        return bookingMapper.toDto(bookingSaved);
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundBookingException(bookingId));
        Item item = booking.getItem();
        User booker = booking.getBooker();
        if (item.getOwner().getId() != userId && booker.getId() != userId) {
            throw new ForbiddenOperationException(userId);
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBooker(long bookerId, BookingState bookingState) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundUserException(bookerId));

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = switch (bookingState) {
            case BookingState.ALL -> bookingRepository.findAllByBooker_Id(bookerId, SORT_NEW_OLD);
            case BookingState.PAST -> bookingRepository.findAllByBooker_IdAndEndBefore(bookerId, now, SORT_NEW_OLD);
            case BookingState.FUTURE -> bookingRepository.findAllByBooker_IdAndStartAfter(bookerId, now, SORT_NEW_OLD);
            case BookingState.CURRENT ->
                    bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(bookerId, now, now, SORT_NEW_OLD);
            case BookingState.WAITING ->
                    bookingRepository.findAllByBooker_IdAndStatus(bookerId, BookingStatus.WAITING, SORT_NEW_OLD);
            case BookingState.REJECTED ->
                    bookingRepository.findAllByBooker_IdAndStatus(bookerId, BookingStatus.REJECTED, SORT_NEW_OLD);
        };
        return bookings.stream().map(bookingMapper::toDto).toList();
    }

    @Override
    public List<BookingDto> getBookingsByOwner(long ownerId, BookingState bookingState) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundUserException(ownerId));
        List<Item> items = itemRepository.findByOwner(owner);
        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = switch (bookingState) {
            case BookingState.ALL -> bookingRepository.findAllByItemOwner_Id(ownerId, SORT_NEW_OLD);
            case BookingState.PAST -> bookingRepository.findAllByItemOwner_IdAndEndBefore(ownerId, now, SORT_NEW_OLD);
            case BookingState.FUTURE ->
                    bookingRepository.findAllByItemOwner_IdAndStartAfter(ownerId, now, SORT_NEW_OLD);
            case BookingState.CURRENT ->
                    bookingRepository.findAllByItemOwner_IdAndStartBeforeAndEndAfter(ownerId, now, now, SORT_NEW_OLD);
            case BookingState.WAITING ->
                    bookingRepository.findAllByItemOwner_IdAndStatus(ownerId, BookingStatus.WAITING, SORT_NEW_OLD);
            case BookingState.REJECTED ->
                    bookingRepository.findAllByItemOwner_IdAndStatus(ownerId, BookingStatus.REJECTED, SORT_NEW_OLD);
        };
        return bookings.stream().map(bookingMapper::toDto).toList();
    }
}
