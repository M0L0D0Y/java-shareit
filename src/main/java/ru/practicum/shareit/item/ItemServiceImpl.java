package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.UserService;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private static final String EMPTY_STRING = "";
    private static final String SPACE_STRING = " ";
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage,
                           UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    @Override
    public Item addItem(long userId, Item item) {
        userService.getUser(userId);
        item.setOwnerId(userId);
        Item savedItem = itemStorage.save(item);
        log.info("Вещь сохранена");
        return savedItem;
    }

    @Override
    public Item updateItem(long userId, long itemId, Item item) {
        userService.getUser(userId);
        Item updateItem = itemStorage.findById(itemId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Вещи с таким id нет " + itemId));
        log.info("Вещь для обновления найдена по id = {}", itemId);
        if (updateItem.getOwnerId() != userId) {
            throw new ForbiddenException("Нет прав для изменения вещи");
        }
        if (item.getName() != null) {
            updateItem.setName(item.getName());
            log.info("Обновили имя");
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
            log.info("Обновили описание");
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
            log.info("Обновили статус");
        }
        Item savedUpdateItem = itemStorage.save(updateItem);
        log.info("Вещь обновлвена");
        return savedUpdateItem;
    }

    @Override
    public Item getItem(long userId, long itemId) {
        userService.getUser(userId);
        Item savedItem = itemStorage.findById(itemId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет вещи с таким id" + itemId));
        log.info("Вещь с id = {} найдена", itemId);
        return savedItem;
    }

    @Override
    public List<Item> getAllItemsByIdOwner(long userId) {
        userService.getUser(userId);
        List<Item> items = itemStorage.findItemByOwnerId(userId);
        log.info("Все вещи пользователя с id = {} найдены", userId);
        return items;
    }

    @Override
    public List<Item> searchItem(long userId, String text) {
        userService.getUser(userId);
        if (text.equals(EMPTY_STRING) || text.equals(SPACE_STRING)) {
            return new LinkedList<>();
        }
        List<Item> items = itemStorage
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text);
        log.info("Вещи по запросу = {} найдены", text);
        return items;
    }
}
