package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Item item);

    Optional<Item> getItemById(long itemId);

    List<Item> getItemsByUser(long userId);

    List<Item> searchItem(String text);

    void deleteItem(long itemId);
}
