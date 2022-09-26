package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.InputComment;
import ru.practicum.shareit.item.dto.InputItem;
import ru.practicum.shareit.validate.Create;
import ru.practicum.shareit.validate.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;


    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(HEADER_USER_ID) Long userId,
                                          @RequestBody @Validated(Create.class) InputItem inputItem) {
        return itemClient.addItem(userId, inputItem);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @RequestBody @Validated(Update.class) InputItem inputItem) {
        return itemClient.updateItem(userId, itemId, inputItem);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                          @PathVariable @Positive Long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItem(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                             @PositiveOrZero @RequestParam(name = "from",
                                                     defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size",
                                                     defaultValue = "10") Integer size) {
        return itemClient.getAllItem(userId, from, size);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> searchItemByText(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                                   @RequestParam String text,
                                                   @PositiveOrZero @RequestParam(name = "from",
                                                           defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size",
                                                           defaultValue = "10") Integer size) {
        return itemClient.searchItemByText(userId, text, from, size);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @RequestBody @Valid InputComment inputComment) {
        return itemClient.addComment(userId, itemId, inputComment);
    }
}
