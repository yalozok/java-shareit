package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

@Component
@Validated
public class ItemMapper {
    public Item toModel(@NotNull @Valid ItemCreateDto itemDto,
                        @NotNull Long ownerId) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(ownerId);
        return item;
    }

    public ItemDto toDto(@NotNull Item item,
                         @NotNull UserDto owner) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        itemDto.setOwner(owner);
        return itemDto;
    }

    public ItemDto toDtoWithRequest(@NotNull Item item,
                                    @NotNull UserDto userDto,
                                    @NotNull ItemRequestDto requestDto) {
        ItemDto dto = this.toDto(item, userDto);
        dto.setRequest(requestDto);
        return dto;
    }
}
