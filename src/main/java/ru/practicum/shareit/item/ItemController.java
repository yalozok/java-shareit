package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class ItemController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    ItemDto createItem(@RequestHeader(SHARER_USER_ID) @PositiveOrZero long userId,
                       @Validated @RequestBody ItemCreateDto itemDto) {
        log.info("==> Create new item: {}", itemDto);
        ItemDto item = itemService.createItem(itemDto, userId);
        log.info("<== Created new item: {}", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@RequestHeader(SHARER_USER_ID) @PositiveOrZero long userId,
                       @PathVariable long itemId,
                       @Validated @RequestBody ItemUpdateDto itemDto) {
        itemDto.setOwner(userId);
        log.info("==> Update item: {}", itemDto);
        ItemDto item = itemService.updateItem(itemDto, itemId);
        log.info("<== Updated item: {}", item);
        return item;
    }

    @GetMapping("/{itemId}")
    ItemDto getItemById(@PathVariable @PositiveOrZero long itemId) {
        log.info("==> Get item: {}", itemId);
        ItemDto item = itemService.getItemById(itemId);
        log.info("<== Get the item: {}", item);
        return item;
    }

    @GetMapping
    List<ItemDto> getItemsByUser(@RequestHeader(SHARER_USER_ID) @PositiveOrZero long userId) {
        log.info("==> Get items by userId: {}", userId);
        List<ItemDto> items = itemService.getItemsByUser(userId);
        log.info("<== Get all items by userId: {}", items);
        return items;
    }

    @GetMapping("/search")
    List<ItemDto> searchItem(@RequestParam @NotNull String text) {
        log.info("==> Search item: {}", text);
        List<ItemDto> items = itemService.searchItem(text);
        log.info("<== Searched item: {}", items);
        return items;
    }

    @DeleteMapping("/{itemId}")
    void deleteItem(@PathVariable long itemId) {
        log.info("==> Delete item: {}", itemId);
        itemService.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto addCommentToItem(@RequestHeader(SHARER_USER_ID) @PositiveOrZero long userId,
                                @Validated @RequestBody CommentCreateDto commentDto,
                                @PathVariable @PositiveOrZero long itemId) {
        log.info("==> Add comment to item: {}", itemId);
        CommentDto comment = itemService.addComment(commentDto, itemId, userId);
        log.info("<== Added comment to item: {}", comment);
        return comment;
    }
}
