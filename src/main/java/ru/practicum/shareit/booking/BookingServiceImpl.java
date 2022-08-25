package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;
    private final UserService userService;
    private final ItemService itemService;

    public BookingServiceImpl(BookingStorage bookingStorage, UserService userService, ItemService itemService) {
        this.bookingStorage = bookingStorage;
        this.userService = userService;
        this.itemService = itemService;
    }


    @Override
    public Booking addBooking(long userId, Booking booking) {
        userService.getUser(userId);
        Item item = itemService.getItem(userId, booking.getItemId());
        if (!item.getAvailable()) {
            throw new UnavailableException("Вещь недоступна для бронирования");
        }
        booking.setBookerId(userId);
        booking.setStatus(Status.WAITING);
        Booking savedBooking = bookingStorage.save(booking);
        log.info("Запрос на бронирование создан");
        return savedBooking;
    }

    @Override
    public Booking approveOwner(long userId, Long bookingId, boolean approved) {
        userService.getUser(userId);
        Booking booking = bookingStorage.findById(bookingId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Запроса на бронирование с таким id нет " + bookingId));
        Item item = itemService.getItem(userId, booking.getItemId());
        if (userId != item.getOwnerId()) {
            throw new ForbiddenException("Статус брони может подтвердить только владелец вещи");
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
        userService.getUser(userId);
        Booking booking = bookingStorage.findById(bookingId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Запроса на бронирование с таким id нет " + bookingId));
        Item item = itemService.getItem(userId, booking.getItemId());
        if ((userId != booking.getBookerId()) && (userId != item.getOwnerId())) {
            throw new ForbiddenException("Бронь может посмотреть только владелец вещи или создатель брони");
        }
        log.info("Запрос на просмотр брони получен");
        return booking;
    }

    @Override
    public List<Booking> getBookingsByBooker(long userId, String state) {
        userService.getUser(userId);
        State getState;
        try {
            getState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Неподдерживаемый статус");
        }

        List<Booking> bookings = null;
        switch (getState) {
            case ALL:
                bookings = bookingStorage.findByBookerIdOrderByStartDesc(userId);
                log.info("Найдены все брони пользователя");
                break;
            case PAST:
                LocalDateTime dateTimePast = LocalDateTime.now();
                bookings = bookingStorage.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, dateTimePast);
                log.info("Найдены все завершенные брони пользователя");
                break;
            case FUTURE:
                LocalDateTime dateTimeFuture = LocalDateTime.now();
                bookings = bookingStorage.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, dateTimeFuture);
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
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByOwner(long userId, String state) {
        userService.getUser(userId);
        State getState;
        try {
            getState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Неподдерживаемый статус");
        }
        List<Booking> bookings = new LinkedList<>();
        List<Item> items = itemService.getAllItemsByIdOwner(userId);
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
                LocalDateTime dateTimePast = LocalDateTime.now();
                for (Item item : items) {
                    List<Booking> allBookings = bookingStorage.findByItemIdAndEndIsBefore(item.getId(), dateTimePast);
                    if (!allBookings.isEmpty()) {
                        bookings.addAll(allBookings);
                    }
                }
                break;
            case FUTURE:
                LocalDateTime dateTimeFuture = LocalDateTime.now();
                for (Item item : items) {
                    List<Booking> allBookings = bookingStorage.findByItemIdAndStartIsAfter(item.getId(), dateTimeFuture);
                    if (!allBookings.isEmpty()) {
                        bookings.addAll(allBookings);
                    }
                }
                break;
            case WAITING:
                for (Item item : items) {
                    List<Booking> allBookings = bookingStorage.findByItemIdAndStatusIs(item.getId(), Status.WAITING);
                    if (!allBookings.isEmpty()) {
                        bookings.addAll(allBookings);
                    }
                }
                break;
            case REJECTED:
                for (Item item : items) {
                    List<Booking> allBookings = bookingStorage.findByItemIdAndStatusIs(item.getId(), Status.REJECTED);
                    if (!allBookings.isEmpty()) {
                        bookings.addAll(allBookings);
                    }
                }
                break;
        }
        if (bookings.size() > 1) {
            bookings.sort((o1, o2) -> o2.getStart().compareTo(o1.getStart()));
        }
        return bookings;
    }
}
