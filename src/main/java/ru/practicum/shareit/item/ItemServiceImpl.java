package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotFoundItemException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepositoryImpl itemRepository;
    private final UserRepositoryImpl userRepository;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto createItem(ItemCreateDto itemDto, long userId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));
        Item savedItem = itemRepository.createItem(itemMapper.toModel(itemDto, userId));
        return itemMapper.toDto(savedItem, userMapper.toDto(user));
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemDto, long itemId) {
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundItemException(itemId));
        if (!itemDto.getOwner().equals(item.getOwner())) {
            throw new ForbiddenOperationException(itemDto.getOwner(), itemId);
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        long userId = item.getOwner();
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundUserException(userId));

        Item savedItem = itemRepository.updateItem(item);
        return itemMapper.toDto(savedItem, userMapper.toDto(user));
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundItemException(itemId));
        User user = userRepository.getUserById(item.getOwner())
                .orElseThrow(() -> new NotFoundUserException(item.getOwner()));

        return itemMapper.toDto(item, userMapper.toDto(user));
    }

    @Override
    public List<ItemDto> getItemsByUser(long userId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));
        List<Item> items = itemRepository.getItemsByUser(userId);
        return items.stream()
                .map(item -> itemMapper.toDto(item, userMapper.toDto(user)))
                .collect(Collectors.toList());
    }


    @Override
    public List<ItemDto> searchItem(String text) {
        List<Item> items = itemRepository.searchItem(text);
        return items.stream()
                .map(item -> {
                    User user = userRepository.getUserById(item.getOwner())
                            .orElseThrow(() -> new NotFoundUserException(item.getOwner()));
                    return itemMapper.toDto(item, userMapper.toDto(user));
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(long itemId) {
        itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundItemException(itemId));
        itemRepository.deleteItem(itemId);
    }

}
