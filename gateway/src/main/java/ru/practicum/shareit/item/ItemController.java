package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    ResponseEntity<Object> updateItem(@RequestHeader(SHARER_USER_ID) @NotNull @PositiveOrZero Long userId,
                                      @PathVariable @NotNull Long itemId,
                                      @Validated @RequestBody ItemUpdateDto itemDto) {
        log.info("==> Update item: {}", itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    ResponseEntity<Object> getItemById(@PathVariable @PositiveOrZero @NotNull Long itemId) {
        log.info("==> Get item: {}", itemId);
        return itemClient.getItemById(itemId);
    }

    @GetMapping
    ResponseEntity<Object> getItemsByUser(@RequestHeader(SHARER_USER_ID) @PositiveOrZero @NotNull Long userId) {
        log.info("==> Get items by userId: {}", userId);
        return itemClient.getItemsByUser(userId);
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
        return itemClient.addComment(commentDto, itemId, userId);
    }
}
