package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.requests.Page;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private static final String EMPTY_STRING = "";
    private static final String SPACE_STRING = " ";

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final CommentStorage commentStorage;
    private final BookingStorage bookingStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage,
                           UserStorage userStorage,
                           CommentStorage commentStorage,
                           BookingStorage bookingStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.commentStorage = commentStorage;
        this.bookingStorage = bookingStorage;
    }

    @Override
    public Item addItem(long userId, Item item) {
        checkExistUser(userId);
        item.setOwnerId(userId);
        Item savedItem = itemStorage.save(item);
        log.info("Вещь сохранена");
        return savedItem;
    }

    @Override
    public Item updateItem(long userId, long itemId, Item item) {
        checkExistUser(userId);
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
        checkExistUser(userId);
        Item savedItem = itemStorage.findById(itemId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет вещи с таким id" + itemId));
        log.info("Вещь с id = {} найдена", itemId);
        return savedItem;
    }

    @Override
    public List<Item> getAllItem(long userId, int from, int size) {
        checkExistUser(userId);
        Pageable pageable = Page.getPageable(from, size);
        List<Item> items = itemStorage.findItemByOwnerId(userId, pageable);
        if (items.size() > 1) {
            items.sort((Comparator.comparing(Item::getId)));
        }
        log.info("Все вещи пользователя с id = {} найдены", userId);
        return items;
    }

    @Override
    public List<Item> searchItemByText(long userId, String text, int from, int size) {
        checkExistUser(userId);
        if (text.equals(EMPTY_STRING) || text.equals(SPACE_STRING)) {
            return new LinkedList<>();
        }
        Pageable pageable = Page.getPageable(from, size);
        List<Item> items = itemStorage.searchItemByText(text, pageable);
        log.info("Вещи по запросу = {} найдены", text);
        return items;
    }

    @Override
    public Comment addComment(long userId, long itemId, Comment comment) {
        checkExistUser(userId);
        getItem(userId, itemId);
        LocalDateTime dateTime = LocalDateTime.now();
        List<Booking> bookings = bookingStorage.findAllPastBookingsByBookerAndItemId(itemId, userId, dateTime);
        if (bookings.isEmpty()) {
            throw new UnavailableException("Пользователь не может оставить отзыв на эту вещь");
        }
        comment.setItemId(itemId);
        comment.setAuthorId(userId);
        comment.setCreated(dateTime);
        Comment savedCommit = commentStorage.save(comment);
        log.info("Коммент сохранен");
        return savedCommit;
    }

    @Override
    public List<Comment> getCommentsByItemID(long itemId) {
        return commentStorage.findByItemId(itemId);
    }

    @Override
    public List<Item> getAllItemByRequestId(long userId, long requestId) {
        checkExistUser(userId);
        return itemStorage.getAllItemByRequestId(requestId);
    }

    private void checkExistUser(long userId) {
        userStorage.findById(userId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет " + userId));
    }
}
