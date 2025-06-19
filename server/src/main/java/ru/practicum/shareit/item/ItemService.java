package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemCreateDto itemCreateDto, long userId);

    ItemDto updateItem(long ownerId, ItemUpdateDto itemCreateDto, long itemId);

    List<ItemDto> getItemsByUser(long userId);

    ItemDto getItemById(long itemId);

    List<ItemDto> searchItem(String text);

    void deleteItem(Long itemId);

    CommentDto addComment(CommentCreateDto commentCreateDto, long itemId, long userId);
}
