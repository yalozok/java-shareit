package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;

    public record ItemDto(long id, String name, long ownerId) {}
}
