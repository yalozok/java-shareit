package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    ItemDto createItem(@RequestHeader(SHARER_USER_ID) @PositiveOrZero Long userId,
                       @Validated @RequestBody ItemCreateDto itemDto) {
        log.info("==> Create new item: {}", itemDto);
        ItemDto item = itemService.createItem(itemDto, userId);
        log.info("<== Create new item: {}", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@RequestHeader(SHARER_USER_ID) @PositiveOrZero Long userId,
                       @PathVariable long itemId,
                       @Validated @RequestBody ItemUpdateDto itemDto) {
        itemDto.setOwner(userId);
        log.info("==> Update item: {}", itemDto);
        ItemDto item = itemService.updateItem(itemDto, itemId);
        log.info("<== Update item: {}", item);
        return item;
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@PathVariable @PositiveOrZero Long itemId) {
        log.info("==> Get item: {}", itemId);
        ItemDto item = itemService.getItemById(itemId);
        log.info("<== Get item: {}", item);
        return item;
    }

    @GetMapping
    List<ItemDto> getItems(@RequestHeader(SHARER_USER_ID) @PositiveOrZero Long userId) {
        log.info("==> Get items by userId: {}", userId);
        List<ItemDto> items = itemService.getItemsByUser(userId);
        log.info("<== Get items by userId: {}", items);
        return items;
    }

    @GetMapping("/search")
    List<ItemDto> searchItem(@RequestParam @NotNull String text) {
        log.info("==> Search item: {}", text);
        List<ItemDto> items = itemService.searchItem(text);
        log.info("<== Search item: {}", items);
        return items;
    }

    @DeleteMapping("/{itemId}")
    void deleteItem(@PathVariable long itemId) {
        log.info("==> Delete item: {}", itemId);
        itemService.deleteItem(itemId);
    }
}
