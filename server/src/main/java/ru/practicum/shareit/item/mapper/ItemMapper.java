package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@Component
public class ItemMapper {
    public Item toModel(ItemCreateDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        return item;
    }

    public Item toModelWithRequest(ItemCreateDto itemDto, User user,
                                   ItemRequest request) {
        Item item = toModel(itemDto, user);
        item.setRequest(request);
        return item;
    }

    public ItemDto toDto(Item item, User owner) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.isAvailable());
        itemDto.setOwner(new ItemDto.UserDto(owner.getId(), owner.getName()));
        return itemDto;
    }

    public ItemDto toDtoWithRequest(Item item, User owner,
                                    ItemRequestDto requestDto) {
        ItemDto dto = this.toDto(item, owner);
        dto.setRequest(requestDto);
        return dto;
    }

    public ItemDto toDtoWithComments(Item item, User owner,
                                     List<CommentDto> comments) {
        ItemDto dto = this.toDto(item, owner);
        dto.setComments(comments);
        return dto;
    }
}
