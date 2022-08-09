package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody Item item) {
        return itemService.addItem(userId, item);
    }

    @PatchMapping(value = "/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable Long itemId,
                           @RequestBody Item item) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping(value = "/{itemId}")
    public Item getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<Item> getAllItem(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItemsByIdOwner(userId);
    }

    @GetMapping(value = "/search")
    public List<Item> searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestParam String text) {
        return itemService.searchItem(userId, text);
    }
}
