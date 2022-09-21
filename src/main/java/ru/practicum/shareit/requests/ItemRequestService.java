package ru.practicum.shareit.requests;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addItemRequest(long userId, ItemRequest itemRequest);

    List<ItemRequest> getAllMeRequest(long userId);

    List<ItemRequest> getAllItemRequest(long userId, int from, int size);

    ItemRequest getItemRequestById(long userId, long requestId);
}
