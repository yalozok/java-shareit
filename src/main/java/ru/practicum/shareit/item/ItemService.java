package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemCreateDto itemCreateDto, long userId);

    ItemDto updateItem(ItemUpdateDto itemCreateDto, long itemId);

    List<ItemDto> getItemsByUser(long userId);

    ItemDto getItemById(long itemId);

    List<ItemDto> searchItem(String text);

    void deleteItem(long itemId);
}
