package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    Item addItem(long userId, Item item);

    Item updateItem(long userId, long itemId, Item item);

    Item getItem(long userId, long id);

    List<Item> getAllItemsByIdOwner(long userId);

    List<Item> searchItem(long userId, String text);
}