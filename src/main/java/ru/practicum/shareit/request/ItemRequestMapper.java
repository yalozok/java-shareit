package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Validated
public class ItemRequestMapper {
    public ItemRequest toModel(@NotNull ItemRequestCreateDto createDto,
                               @NotNull User requestor) {
        ItemRequest request = new ItemRequest();
        request.setDescription(createDto.getDescription());
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());
        return request;
    }

    public ItemRequestDto toDto(@NotNull ItemRequest request) {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        return requestDto;
    }

    public ItemRequestDto toDtoWithItems(@NotNull ItemRequest request,
                                           @NotNull List<Item> items) {
        ItemRequestDto requestDto = toDto(request);
        requestDto.setItems(items
                .stream()
                .map(item ->
                        new ItemRequestDto.ItemDto(
                                item.getId(), item.getName(), item.getOwner().getId()))
                .toList());
        return requestDto;
    }
}
