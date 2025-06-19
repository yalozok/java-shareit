package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.time.LocalDateTime;

public class TestDto {
    public static UserCreateDto userToCreate = new UserCreateDto("Alice Smith", "alice@example.com");
    public static UserCreateDto userToCreateWithExistingEmail = new UserCreateDto("Alice Doe", "alice@example.com");
    public static UserUpdateDto userToUpdate = new UserUpdateDto(null, "alice.smith@example.com");

    public static ItemCreateDto itemCreateDto = new ItemCreateDto("Phone", "Phone with disc", true, null);
    public static ItemUpdateDto itemUpdateDto = new ItemUpdateDto("Drill electric", null, null, TestData.owner.getId());
    public static ItemUpdateDto itemUpdateDtoByNotOwner = new ItemUpdateDto("Drill electric", null, null, TestData.requestor1.getId());

    public static BookingCreateDto bookingCreateDto = new BookingCreateDto(TestData.item1.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5));
    public static BookingCreateDto bookingCreateDtoNotAvailable = new BookingCreateDto(TestData.item3.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5));

    public static CommentCreateDto commentCreateDto = new CommentCreateDto("Awesome!");
    public static ItemRequestCreateDto requestCreateDto = new ItemRequestCreateDto("Need a phone with disc");
}
