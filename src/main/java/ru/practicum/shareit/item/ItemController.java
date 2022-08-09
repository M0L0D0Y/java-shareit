package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return itemMapper.toItemDto(itemService.addItem(userId, item));
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return itemMapper.toItemDto(itemService.updateItem(userId, itemId, item));
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long itemId) {
        return itemMapper.toItemDto(itemService.getItem(userId, itemId));
    }

    @GetMapping
    public List<ItemDto> getAllItem(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<Item> itemList = itemService.getAllItemsByIdOwner(userId);
        List<ItemDto> itemDtoList = new LinkedList<>();
        for (Item item : itemList) {
            itemDtoList.add(itemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    @GetMapping(value = "/search")
    public List<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestParam String text) {
        List<Item> itemList = itemService.searchItem(userId, text);
        List<ItemDto> itemDtoList = new LinkedList<>();
        for (Item item : itemList) {
            itemDtoList.add(itemMapper.toItemDto(item));
        }
        return itemDtoList;
    }
}
