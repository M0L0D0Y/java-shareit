package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.LastBooking;
import ru.practicum.shareit.booking.NextBooking;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.UserService;

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
    private final UserService userService;
    private final BookingStorage bookingStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage,
                           UserService userService, BookingStorage bookingStorage) {
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.bookingStorage = bookingStorage;
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
    public ItemDtoForOwner getItem(long userId, long itemId) {
        userService.getUser(userId);
        Item savedItem = itemStorage.findById(itemId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет вещи с таким id" + itemId));
        ItemDtoForOwner itemDtoForOwner = toItemDtoForOwner(savedItem);
        if (userId == savedItem.getOwnerId()) {
            return addDataTime(itemDtoForOwner, itemId);
        }
        log.info("Вещь с id = {} найдена", itemId);
        return itemDtoForOwner;
    }

    @Override
    public List<ItemDtoForOwner> getAllItemsForOwner(long userId) {
        userService.getUser(userId);
        List<Item> items = itemStorage.findItemByOwnerId(userId);
        List<ItemDtoForOwner> itemDtoForOwnerList = new LinkedList<>();
        for (Item item : items) {
            ItemDtoForOwner itemDtoForOwner = toItemDtoForOwner(item);
            ItemDtoForOwner itemDtoForOwnerWithDate = addDataTime(itemDtoForOwner, item.getId());
            itemDtoForOwnerList.add(itemDtoForOwnerWithDate);
            itemDtoForOwnerList.sort(Comparator.comparing(ItemDtoForOwner::getId));
        }
        log.info("Все вещи пользователя с id = {} найдены", userId);
        return itemDtoForOwnerList;
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

    @Override
    public List<Item> getAllItemsByIdOwner(long userId) {
        userService.getUser(userId);
        List<Item> items = itemStorage.findItemByOwnerId(userId);
        log.info("Все вещи пользователя с id = {} найдены", userId);
        return items;
    }

    private ItemDtoForOwner toItemDtoForOwner(Item item) {
        ItemDtoForOwner itemDtoForOwner = new ItemDtoForOwner();
        itemDtoForOwner.setId(item.getId());
        itemDtoForOwner.setName(item.getName());
        itemDtoForOwner.setDescription(item.getDescription());
        itemDtoForOwner.setAvailable(item.getAvailable());
        return itemDtoForOwner;
    }

    private ItemDtoForOwner addDataTime(ItemDtoForOwner itemDtoForOwner, long itemId) {
        List<Booking> bookings = bookingStorage.findByItemId(itemId);
        if (!bookings.isEmpty()) {
            LocalDateTime dateTime = LocalDateTime.now();
            List<Booking> allLastBookings = bookingStorage.findByItemIdAndEndIsBefore(itemId, dateTime);
            if (!allLastBookings.isEmpty()) {
                if (allLastBookings.size() > 1) {
                    allLastBookings.sort((o1, o2) -> o2.getStart().compareTo(o1.getStart()));
                    LastBooking lastBooking = new LastBooking();
                    lastBooking.setId(allLastBookings.get(allLastBookings.size() - 1).getId());
                    lastBooking.setBookerId(allLastBookings.get(allLastBookings.size() - 1).getBookerId());
                    itemDtoForOwner.setLastBooking(lastBooking);
                }
                LastBooking lastBooking = new LastBooking();
                lastBooking.setId(allLastBookings.get(0).getId());
                lastBooking.setBookerId(allLastBookings.get(0).getBookerId());
                itemDtoForOwner.setLastBooking(lastBooking);
            }
            List<Booking> allNextBookings = bookingStorage.findByItemIdAndStartIsAfter(itemId, dateTime);
            if (!allNextBookings.isEmpty()) {
                if (bookings.size() > 1) {
                    bookings.sort((o1, o2) -> o2.getStart().compareTo(o1.getStart()));
                }
                NextBooking nextBooking = new NextBooking();
                nextBooking.setId(allNextBookings.get(0).getId());
                nextBooking.setBookerId(allNextBookings.get(0).getBookerId());
                itemDtoForOwner.setNextBooking(nextBooking);
            }
        }
        return itemDtoForOwner;
    }
}
