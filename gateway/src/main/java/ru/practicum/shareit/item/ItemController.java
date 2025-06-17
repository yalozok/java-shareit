package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Slf4j
@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    ResponseEntity<Object> createItem(@RequestHeader(SHARER_USER_ID) @NotNull @PositiveOrZero Long userId,
                              @Validated @RequestBody ItemCreateDto itemDto) {
        log.info("==> Create new item: {}", itemDto);
        ResponseEntity<Object> item = itemClient.createItem(userId, itemDto);
        log.info("<== Created new item: {}", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    ResponseEntity<Object> updateItem(@RequestHeader(SHARER_USER_ID ) @NotNull @PositiveOrZero Long userId,
                       @PathVariable @NotNull Long itemId,
                       @Validated @RequestBody ItemUpdateDto itemDto) {
        itemDto.setOwner(userId);
        log.info("==> Update item: {}", itemDto);
        ResponseEntity<Object> item = itemClient.updateItem(itemId, itemDto);
        log.info("<== Updated item: {}", item);
        return item;
    }

    @GetMapping("/{itemId}")
    ResponseEntity<Object> getItemById(@PathVariable @PositiveOrZero @NotNull Long itemId) {
        log.info("==> Get item: {}", itemId);
        ResponseEntity<Object> item = itemClient.getItemById(itemId);
        log.info("<== Get the item: {}", item);
        return item;
    }

    @GetMapping
    ResponseEntity<Object> getItemsByUser(@RequestHeader(SHARER_USER_ID) @PositiveOrZero @NotNull Long userId) {
        log.info("==> Get items by userId: {}", userId);
        ResponseEntity<Object> items = itemClient.getItemsByUser(userId);
        log.info("<== Get all items by userId: {}", items);
        return items;
    }

    @GetMapping("/search")
    ResponseEntity<Object> searchItem(@RequestParam @NotNull String text) {
        log.info("==> Search item: {}", text);
        ResponseEntity<Object> items = itemClient.searchItem(text);
        log.info("<== Searched item: {}", items);
        return items;
    }

    @DeleteMapping("/{itemId}")
    void deleteItem(@PathVariable Long itemId) {
        log.info("==> Delete item: {}", itemId);
        itemClient.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> addCommentToItem(@RequestHeader(SHARER_USER_ID) @PositiveOrZero @NotNull Long userId,
                                @Validated @RequestBody CommentCreateDto commentDto,
                                @PathVariable @PositiveOrZero @NotNull Long itemId) {
        log.info("==> Add comment to item: {}", itemId);
        ResponseEntity<Object> comment = itemClient.addComment(commentDto, itemId, userId);
        log.info("<== Added comment to item: {}", comment);
        return comment;
    }
}
