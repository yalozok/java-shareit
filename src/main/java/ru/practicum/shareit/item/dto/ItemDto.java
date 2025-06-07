package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private UserDto owner;
    private ItemRequestDto request;
    private List<CommentDto> comments;
    private BookingDto lastBooking;
    private BookingDto nextBooking;

    public record BookingDto(long bookingId, long bookerId) {
    }

    public record UserDto(long userId, String userName) {
    }
}
