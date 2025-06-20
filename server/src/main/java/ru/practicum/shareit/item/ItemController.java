package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    ItemDto createItem(@RequestHeader(SHARER_USER_ID) long userId,
                       @RequestBody ItemCreateDto itemDto) {
        log.info("==> Create new item: {}", itemDto);
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@RequestHeader(SHARER_USER_ID) long userId,
            @PathVariable long itemId,
                       @RequestBody ItemUpdateDto itemDto) {
        log.info("==> Update item: {}", itemDto);
        return itemService.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    ItemDto getItemById(@PathVariable long itemId) {
        log.info("==> Get item: {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    List<ItemDto> getItemsByUser(@RequestHeader(SHARER_USER_ID) long userId) {
        log.info("==> Get items by userId: {}", userId);
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    List<ItemDto> searchItem(@RequestParam String text) {
        log.info("==> Search item: {}", text);
        return itemService.searchItem(text);
    }

    @DeleteMapping("/{itemId}")
    void deleteItem(@PathVariable long itemId) {
        log.info("==> Delete item: {}", itemId);
        itemService.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto addCommentToItem(@RequestHeader(SHARER_USER_ID) long userId,
                                @RequestBody CommentCreateDto commentDto,
                                @PathVariable long itemId) {
        log.info("==> Add comment to item: {}", itemId);
        return itemService.addComment(commentDto, itemId, userId);
    }
}
