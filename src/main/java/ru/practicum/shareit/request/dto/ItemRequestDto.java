package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequestDto {
    private long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
}
