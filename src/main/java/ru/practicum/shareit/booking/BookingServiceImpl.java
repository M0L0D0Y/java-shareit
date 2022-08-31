package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Autowired
    public BookingServiceImpl(BookingStorage bookingStorage,
                              UserStorage userStorage,
                              ItemStorage itemStorage) {
        this.bookingStorage = bookingStorage;
        this.userStorage = userStorage;
        this.itemStorage = itemStorage;
    }


    @Override
    public Booking addBooking(long userId, Booking booking) {
        checkExistUser(userId);
        Item item = checkExistItem(booking.getItemId());
        if (!item.getAvailable()) {
            throw new UnavailableException("Вещь недоступна для бронирования");
        }
        if (userId == item.getOwnerId()) {
            throw new NotFoundException("Хозяин вещи не может брать в аренду свои вещи");
        }
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new UnavailableException("Старт брони не может быть после ее окончания");
        }
        booking.setBookerId(userId);
        booking.setStatus(Status.WAITING);
        Booking savedBooking = bookingStorage.save(booking);
        log.info("Запрос на бронирование создан");
        return savedBooking;
    }

    @Override
    public Booking replyFromOwner(long userId, long bookingId, boolean approved) {
        checkExistUser(userId);
        Booking booking = bookingStorage.findById(bookingId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Запроса на бронирование с таким id нет " + bookingId));
        Item item = checkExistItem(booking.getItemId());
        if (userId != item.getOwnerId()) {
            throw new NotFoundException("Статус брони может подтвердить только владелец вещи");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new UnavailableException("Бронь уже подтверждена");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        }
        if (!approved) {
            booking.setStatus(Status.REJECTED);
        }
        bookingStorage.save(booking);
        log.info("Владец вещи ответил на запрос бронирования");
        return booking;
    }

    @Override
    public Booking getBooking(long userId, long bookingId) {
        checkExistUser(userId);
        Booking booking = bookingStorage.findById(bookingId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Запроса на бронирование с таким id нет " + bookingId));
        Item item = checkExistItem(booking.getItemId());
        if ((userId != booking.getBookerId()) && (userId != item.getOwnerId())) {
            throw new NotFoundException("Бронь может посмотреть только владелец вещи или создатель брони");
        }
        log.info("Запрос на просмотр брони получен");
        return booking;
    }

    @Override
    public List<Booking> getBookingsBooker(long userId, String state) {
        checkExistUser(userId);
        State getState;
        try {
            getState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Неподдерживаемый статус");
        }
        LocalDateTime dateTime = LocalDateTime.now();
        List<Booking> bookings = null;
        switch (getState) {
            case ALL:
                bookings = bookingStorage.findByBookerIdOrderByStartDesc(userId);
                log.info("Найдены все брони пользователя");
                break;
            case PAST:
                bookings = bookingStorage.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, dateTime);
                log.info("Найдены все завершенные брони пользователя");
                break;
            case FUTURE:
                bookings = bookingStorage.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, dateTime);
                log.info("Найдены все будущие брони пользователя");
                break;
            case WAITING:
                bookings = bookingStorage.findByBookerIdAndStatusIsOrderByStartDesc(userId, Status.WAITING);
                log.info("Найдены все брони пользователя, ожидающие подтверждения");
                break;
            case REJECTED:
                bookings = bookingStorage.findByBookerIdAndStatusIsOrderByStartDesc(userId, Status.REJECTED);
                log.info("Найдены все отклоненные брони пользователя");
                break;
            case CURRENT:
                bookings = bookingStorage.
                        findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, dateTime, dateTime);
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsOwner(long userId, String state) {
        checkExistUser(userId);
        State getState;
        try {
            getState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Неподдерживаемый статус");
        }
        List<Booking> bookings = new LinkedList<>();
        List<Item> items = itemStorage.findItemByOwnerId(userId);
        LocalDateTime dateTime = LocalDateTime.now();
        if (!items.isEmpty()) {
            switch (getState) {
                case ALL:
                    for (Item item : items) {
                        List<Booking> allBookings = bookingStorage.findByItemId(item.getId());
                        if (!allBookings.isEmpty()) {
                            bookings.addAll(allBookings);
                        }
                    }
                    break;
                case PAST:
                    for (Item item : items) {
                        List<Booking> allBookings = bookingStorage
                                .findByItemIdAndEndIsBefore(item.getId(), dateTime);
                        if (!allBookings.isEmpty()) {
                            bookings.addAll(allBookings);
                        }
                    }
                    break;
                case FUTURE:
                    for (Item item : items) {
                        List<Booking> allBookings = bookingStorage
                                .findByItemIdAndStartIsAfter(item.getId(), dateTime);
                        if (!allBookings.isEmpty()) {
                            bookings.addAll(allBookings);
                        }
                    }
                    break;
                case WAITING:
                    for (Item item : items) {
                        List<Booking> allBookings = bookingStorage
                                .findByItemIdAndStatusIs(item.getId(), Status.WAITING);
                        if (!allBookings.isEmpty()) {
                            bookings.addAll(allBookings);
                        }
                    }
                    break;
                case REJECTED:
                    for (Item item : items) {
                        List<Booking> allBookings = bookingStorage
                                .findByItemIdAndStatusIs(item.getId(), Status.REJECTED);
                        if (!allBookings.isEmpty()) {
                            bookings.addAll(allBookings);
                        }
                    }
                    break;
                case CURRENT:
                    for (Item item : items) {
                        List<Booking> allBookings = bookingStorage.
                                findByItemIdAndStartIsBeforeAndEndIsAfter(item.getId(), dateTime, dateTime);
                        if (!allBookings.isEmpty()) {
                            bookings.addAll(allBookings);
                        }
                    }
                    break;
            }
        }
        if (bookings.size() > 1) {
            bookings.sort((o1, o2) -> o2.getStart().compareTo(o1.getStart()));
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByItemId(long itemId) {
        return bookingStorage.findByItemId(itemId);
    }

    @Override
    public List<Booking> getBookingsByItemIdForPastState(long itemId, LocalDateTime dateTime) {
        return bookingStorage.findByItemIdAndEndIsBefore(itemId, dateTime);

    }

    @Override
    public List<Booking> getBookingsByItemIdForFutureState(long itemId, LocalDateTime dateTime) {
        return bookingStorage.findByItemIdAndStartIsAfter(itemId, dateTime);
    }

    @Override
    public List<Booking> getBookingsUsedByUser(long itemId, long userId, LocalDateTime dateTime) {
        return bookingStorage.findByItemIdAndBookerIdAndEndIsBefore(itemId, userId, dateTime);
    }

    private void checkExistUser(long userId) {
        userStorage.findById(userId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет " + userId));
    }

    private Item checkExistItem(long itemId) {
        return itemStorage.findById(itemId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет вещи с таким id" + itemId));
    }
}
