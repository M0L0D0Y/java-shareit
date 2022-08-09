package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto item);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto getItem(long userId, long id);

    List<ItemDto> getAllItemsByIdOwner(long userId);

    List<ItemDto> searchItem(long userId, String text);
}
