package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.dto.ItemMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(HEADER_USER_ID) long userId, @Valid @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return itemMapper.toItemDto(itemService.addItem(userId, item));
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@RequestHeader(HEADER_USER_ID) long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return itemMapper.toItemDto(itemService.updateItem(userId, itemId, item));
    }

    @GetMapping(value = "/{itemId}")
    public ItemDtoForOwner getItem(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable Long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoForOwner> getAllItem(@RequestHeader(HEADER_USER_ID) long userId) {
        return itemService.getAllItemsForOwner(userId);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> searchItem(@RequestHeader(HEADER_USER_ID) long userId,
                                    @RequestParam String text) {
        return itemService.searchItem(userId, text)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
