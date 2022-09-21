package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.requests.Page;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
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
    @Transactional
    public Booking addBooking(long userId, Booking booking) {
        checkExistUser(userId);
        Item item = checkExistItem(booking.getItemId());
        if (!item.getAvailable()) {
            throw new UnavailableException("Вещь недоступна для бронирования");
        }
        if (userId == item.getOwner().getId()) {
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
    @Transactional
    public Booking confirmBookingByOwner(long userId, long bookingId, boolean approved) {
        checkExistUser(userId);
        Booking booking = bookingStorage.findById(bookingId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Запроса на бронирование с таким id нет " + bookingId));
        Item item = checkExistItem(booking.getItemId());
        if (userId != item.getOwner().getId()) {
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
        if ((userId != booking.getBookerId()) && (userId != item.getOwner().getId())) {
            throw new NotFoundException("Бронь может посмотреть только владелец вещи или создатель брони");
        }
        log.info("Запрос на просмотр брони получен");
        return booking;
    }

    @Override
    public List<Booking> getBookingsByBookerId(long userId, String state, int from, int size) {
        checkExistUser(userId);
        State getState;
        try {
            getState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Неподдерживаемый статус " + state);
        }
        Pageable pageable = Page.getPageable(from, size);
        LocalDateTime dateTime = LocalDateTime.now();
        List<Booking> bookings = null;
        switch (getState) {
            case ALL:
                bookings = bookingStorage.findAllBookingsByBookerId(userId, pageable);
                log.info("Найдены все брони пользователя");
                System.out.println(bookings);
                break;
            case PAST:
                bookings = bookingStorage.findAllPastBookingsByBookerId(userId, dateTime, pageable);
                log.info("Найдены все завершенные брони пользователя");
                break;
            case FUTURE:
                bookings = bookingStorage.findAllFutureBookingsByBookerId(userId, dateTime, pageable);
                log.info("Найдены все будущие брони пользователя");
                break;
            case WAITING:
                bookings = bookingStorage.findByStatusAllBookingsByBookerId(userId, Status.WAITING, pageable);
                log.info("Найдены все брони пользователя, ожидающие подтверждения");
                break;
            case REJECTED:
                bookings = bookingStorage.findByStatusAllBookingsByBookerId(userId, Status.REJECTED, pageable);
                log.info("Найдены все отклоненные брони пользователя");
                break;
            case CURRENT:
                bookings = bookingStorage.findAllCurrentBookingsByBookerId(userId, dateTime, pageable);
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByIdOwnerItem(long userId, String state, int from, int size) {
        checkExistUser(userId);
        State getState;
        try {
            getState = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStatusException("Неподдерживаемый статус");
        }
        Pageable pageable = Page.getPageable(from, size);
        List<Booking> bookings = null;
        List<Item> items = itemStorage.findAllItemByOwnerId(userId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (!items.isEmpty()) {
            switch (getState) {
                case ALL:
                    bookings = bookingStorage.findByIdOwnerItem(userId, pageable);
                    break;
                case PAST:
                    bookings = bookingStorage.findAllPastBookingsByIdOwnerItem(userId, currentDateTime, pageable);
                    break;
                case FUTURE:
                    bookings = bookingStorage.findAllFutureBookingsByIdOwnerItem(userId, currentDateTime, pageable);
                    break;
                case WAITING:
                    bookings = bookingStorage.findByIdOwnerItemAndStatusIs(userId, Status.WAITING, pageable);
                    break;
                case REJECTED:
                    bookings = bookingStorage.findByIdOwnerItemAndStatusIs(userId, Status.REJECTED, pageable);
                    break;
                case CURRENT:
                    bookings = bookingStorage
                            .findAllCurrentBookingByIdOwnerItem(userId, currentDateTime, pageable);
                    break;
            }
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByItemId(long itemId) {
        return bookingStorage.findByItemId(itemId);
    }

    @Override
    public List<Booking> findAllPastBookingsByItemId(long itemId, LocalDateTime currentDateTime) {
        return bookingStorage.findAllPastBookingsByItemId(itemId, currentDateTime);

    }

    @Override
    public List<Booking> findAllFutureBookingsByItemId(long itemId, LocalDateTime currentDateTime) {
        return bookingStorage.findAllFutureBookingsByItemId(itemId, currentDateTime);
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
