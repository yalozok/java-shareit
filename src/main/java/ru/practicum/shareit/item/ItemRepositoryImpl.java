package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> itemsByUser = new HashMap<>();
    private long id = 0;

    @Override
    public Item createItem(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);

        long userId = item.getOwner();
        List<Item> listItemsByUser = itemsByUser.containsKey(userId) ?
                itemsByUser.get(userId) : new ArrayList<>();
        listItemsByUser.add(item);
        itemsByUser.put(userId, listItemsByUser);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> getItemById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getItemsByUser(long userId) {
        return itemsByUser.containsKey(userId) ?
                itemsByUser.get(userId) : new ArrayList<>();
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text == null || text.isEmpty()) return new ArrayList<>();

        String lowerCaseText = text.toLowerCase();
        return items.values().stream()
                .filter(i -> (i.getName().toLowerCase().contains(lowerCaseText) ||
                        i.getDescription().toLowerCase().contains(lowerCaseText)) && i.isAvailable())
                .toList();
    }

    @Override
    public void deleteItem(long itemId) {
        Item item = items.remove(itemId);
        long userId = item.getOwner();
        List<Item> listItemsByUser = itemsByUser.get(userId);
        if (listItemsByUser != null) {
            listItemsByUser.removeIf(i -> i.getId() == itemId);

            if (listItemsByUser.isEmpty()) {
                itemsByUser.remove(userId);
            }
        }
    }
}
