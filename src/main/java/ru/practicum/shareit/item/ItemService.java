package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDtoForOwner;

import java.util.List;

public interface ItemService {
    Item addItem(long userId, Item item);

    Item updateItem(long userId, long itemId, Item item);

    ItemDtoForOwner getItem(long userId, long id);

    List<ItemDtoForOwner> getAllItemsForOwner(long userId);

    List<Item> searchItem(long userId, String text);
    List<Item> getAllItemsByIdOwner(long userId);

}
