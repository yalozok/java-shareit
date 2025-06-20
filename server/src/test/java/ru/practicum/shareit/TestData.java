package ru.practicum.shareit;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class TestData {

    public static User owner = new User(1, "Alice Smith", "alice@example.com");
    public static User requestor1 = new User(2, "Bob Johnson", "bob@example.com");
    public static User requestor2 = new User(3, "Charlie Brown", "charlie@example.com");

    public static ItemRequest request1 = new ItemRequest(1, "Request for a drill", requestor1, LocalDateTime.parse("2024-01-10T10:00:00"));
    public static ItemRequest request2 = new ItemRequest(2, "Need a ladder for painting", requestor2, LocalDateTime.parse("2024-01-15T15:30:00"));
    public static ItemRequest request3 = new ItemRequest(3, "Anybody has an extra oven?", requestor1, LocalDateTime.parse("2024-05-01T10:00:00"));
    public static List<ItemRequest> requestsByRequestor1 = List.of(request1, request3);

    public static Item item1 = new Item(1, "Drill", "Electric drill with variable speed", true, owner, request1);
    public static Item item2 = new Item(2, "Ladder", "10-foot aluminum ladder", true, owner, request2);
    public static Item item3 = new Item(3, "Hammer", "Standard claw hammer", false, owner, null);

    public static Booking booking1 = new Booking(1, LocalDateTime.parse("2024-02-01T09:00:00"), LocalDateTime.parse("2024-02-01T17:00:00"), item1, requestor1, BookingStatus.APPROVED);
    public static Booking booking2 = new Booking(2, LocalDateTime.parse("2024-02-05T14:00:00"), LocalDateTime.parse("2024-02-06T14:00:00"), item2, requestor2, BookingStatus.WAITING);
    public static Booking booking3 = new Booking(3, LocalDateTime.parse("2024-03-01T08:00:00"), LocalDateTime.parse("2024-03-01T12:00:00"), item3, requestor1, BookingStatus.REJECTED);
}
