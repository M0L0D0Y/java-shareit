package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    Item addItem(long userId, Item item);

    Item updateItem(long userId, long itemId, Item item);

    Item getItem(long userId, long id);

    List<Item> getAllItem(long userId);

    List<Item> searchItemByText(long userId, String text);

    Comment addComment(long userId, long itemId, Comment comment);

    List<Comment> getCommentsByItemID(long itemId);

    List<Item> getAllItemByRequestId(long userId, long requestId);
}
