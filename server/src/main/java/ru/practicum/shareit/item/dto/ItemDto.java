package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public record BookingDto(long bookingId, LocalDateTime start, LocalDateTime end) {
    }

    public record UserDto(long userId, String userName) {
    }
}
