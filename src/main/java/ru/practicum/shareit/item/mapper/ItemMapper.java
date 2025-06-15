package ru.practicum.shareit.item.mapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@Component
@Validated
public class ItemMapper {
    public Item toModel(@NotNull @Valid ItemCreateDto itemDto,
                        @NotNull User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        return item;
    }

    public Item toModelWithRequest(@NotNull @Valid ItemCreateDto itemDto,
                                   @NotNull User user,
                                   @NotNull ItemRequest request) {
        Item item = toModel(itemDto, user);
        item.setRequest(request);
        return item;
    }

    public ItemDto toDto(@NotNull Item item,
                         @NotNull User owner) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        itemDto.setOwner(new ItemDto.UserDto(owner.getId(), owner.getName()));
        return itemDto;
    }

    public ItemDto toDtoWithRequest(@NotNull Item item,
                                    @NotNull User owner,
                                    @NotNull ItemRequestDto requestDto) {
        ItemDto dto = this.toDto(item, owner);
        dto.setRequest(requestDto);
        return dto;
    }

    public ItemDto toDtoWithComments(@NotNull Item item,
                                     @NotNull User owner,
                                     @NotNull List<CommentDto> comments) {
        ItemDto dto = this.toDto(item, owner);
        dto.setComments(comments);
        return dto;
    }
}
