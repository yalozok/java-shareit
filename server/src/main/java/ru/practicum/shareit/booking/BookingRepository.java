package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    //all
    List<Booking> findAllByBooker_Id(long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId ORDER BY b.start DESC")
    List<Booking> findAllByOwner_Id(@Param("ownerId") long ownerId);

    //current: start < now < end
    @Query("""
            SELECT b FROM Booking b WHERE b.booker.id = :bookerId
            AND b.start < :start AND b.end > :end ORDER BY b.start DESC
            """)
    List<Booking> findCurrentByBookerId(
            @Param("bookerId") long bookerId, LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId
            AND b.start < :start AND b.end > :end ORDER BY b.start DESC
            """)
    List<Booking> findCurrentByOwnerId(
            @Param("ownerId") long ownerId, LocalDateTime start, LocalDateTime end);

    //past: end < now
    @Query("""
            SELECT b FROM Booking b WHERE b.booker.id = :bookerId
            AND b.end < :end ORDER BY b.start DESC
            """)
    List<Booking> findPastByBookerId(
            @Param("bookerId") long bookerId, LocalDateTime end);

    @Query("""
            SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId
            AND b.end < :end ORDER BY b.start DESC
            """)
    List<Booking> findPastByOwnerId(
            @Param("ownerId") long ownerId, LocalDateTime end);

    //future: now < start
    @Query("""
            SELECT b FROM Booking b WHERE b.booker.id = :bookerId
            AND b.start > :start ORDER BY b.start DESC
            """)
    List<Booking> findFutureByBookerId(
            @Param("bookerId") long bookerId, LocalDateTime start);

    @Query("""
            SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId
            AND b.start > :start ORDER BY b.start DESC
            """)
    List<Booking> findFutureByItemOwnerId(
            @Param("ownerId") long ownerId, LocalDateTime start);

    List<Booking> findAllByBooker_IdAndStatus(long bookerId, BookingStatus status, Sort sort);

    @Query("""
            SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId
                AND b.status = :status ORDER BY b.start DESC
            """)
    List<Booking> findAllByOwnerIdAndStatus(@Param("ownerId") long ownerId, @Param("status") BookingStatus status);

    @Query("""
                SELECT b FROM Booking b WHERE b.item.id = :itemId
                AND b.end < :now ORDER BY b.end DESC
            """)
    Optional<Booking> findLastBooking(@Param("itemId") long itemId, @Param("now") LocalDateTime now);

    @Query("""
                SELECT b FROM Booking b WHERE b.item.id = :itemId
                AND b.start > :now ORDER BY b.start ASC
            """)
    Optional<Booking> findNextBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    Optional<Booking> findByItem_IdAndBooker_Id(long itemId, long bookerId);

    void deleteByItem(Item item);
}
