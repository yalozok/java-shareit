package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    //all
    List<Booking> findAllByBooker_Id(long bookerId, Sort sort);

    List<Booking> findAllByItemOwner_Id(long ownerId, Sort sort);

    //current: start < now < end
    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfter(
            long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwner_IdAndStartBeforeAndEndAfter(
            long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    //past: end < now
    List<Booking> findAllByBooker_IdAndEndBefore(
            long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwner_IdAndEndBefore(
            long bookerId, LocalDateTime end, Sort sort);

    //future: now < start
    List<Booking> findAllByBooker_IdAndStartAfter(
            long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByItemOwner_IdAndStartAfter(
            long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findAllByBooker_IdAndStatus(long bookerId, BookingStatus status, Sort sort);

    List<Booking> findAllByItemOwner_IdAndStatus(long bookerId, BookingStatus status, Sort sort);

    @Query("""
                SELECT b FROM Booking b
                WHERE b.item.id = :itemId
                  AND b.end < :now
                ORDER BY b.end DESC
            """)
    Optional<Booking> findLastBooking(@Param("itemId") long itemId, @Param("now") LocalDateTime now);

    @Query("""
                SELECT b FROM Booking b
                WHERE b.item.id = :itemId
                  AND b.start > :now
                ORDER BY b.start ASC
            """)
    Optional<Booking> findNextBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    Optional<Booking> findByItem_IdAndBooker_Id(long itemId, long bookerId);
}
