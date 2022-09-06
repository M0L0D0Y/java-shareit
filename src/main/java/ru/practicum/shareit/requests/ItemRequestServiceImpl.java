package ru.practicum.shareit.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;

    @Autowired

    public ItemRequestServiceImpl(ItemRequestStorage itemRequestStorage,
                                  UserStorage userStorage) {
        this.itemRequestStorage = itemRequestStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemRequest addItemRequest(long userId, ItemRequest itemRequest) {
        checkExistUser(userId);
        itemRequest.setRequestor(userId);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest savedItemRequest = itemRequestStorage.save(itemRequest);
        log.info("Запрос вещи сохранен");
        return savedItemRequest;
    }

    @Override
    public List<ItemRequest> getAllMeRequest(long userId) {
        checkExistUser(userId);
        List<ItemRequest> foundRequests = itemRequestStorage.getAllMeRequest(userId);
        log.info("Все запросы пользователя найдены");
        return foundRequests;
    }

    @Override
    public List<ItemRequest> getAllItemRequest(long userId, int from, int size) {
        checkExistUser(userId);
        Pageable pageable = Page.getPageable(from, size);
        List<ItemRequest> foundRequests = itemRequestStorage.getAllRequest(userId, pageable);
        log.info("Все запросы других пользователей найдены");
        return foundRequests;
    }

    @Override
    public ItemRequest getItemRequestById(long userId, long requestId) {
        checkExistUser(userId);
        ItemRequest foundItemRequest = itemRequestStorage.findById(requestId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет запроса с таким id " + requestId));
        log.info("запрос с Id = {} найден", requestId);
        return foundItemRequest;
    }

    private void checkExistUser(long userId) {
        userStorage.findById(userId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет " + userId));
    }
}
