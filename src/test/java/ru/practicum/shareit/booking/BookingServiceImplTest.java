package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {
    BookingService bookingService;
    BookingStorage bookingStorage;
    UserStorage userStorage;
    ItemStorage itemStorage;
    User user1;
    User user2;
    User user3;
    Item item1;
    Item item2;
    Item item3;
    Booking booking1;
    Booking booking2;
    Booking booking3;

    @BeforeEach
    void beforeEach() {
        userStorage = mock(UserStorage.class);
        itemStorage = mock(ItemStorage.class);
        bookingStorage = mock(BookingStorage.class);
        bookingService = new BookingServiceImpl(bookingStorage, userStorage, itemStorage);
        user1 = new User(1L, "user1", "user1@mail.ru");
        item1 = new Item(1L, "item1", "description1", true, user1, null);
        user2 = new User(2L, "user2", "user2@mail.ru");
        item2 = new Item(2L, "item2", "description2", true, user2, null);
        user3 = new User(3L, "user3", "user3@mail.ru");
        item3 = new Item(3L, "item3", "description3", false, user3, null);
        booking1 = new Booking(1L,
                LocalDateTime.of(2022, 8, 1, 12, 0),
                LocalDateTime.of(2022, 8, 2, 12, 0),
                item1.getId(), user2.getId(), Status.APPROVED);
        booking2 = new Booking(2L,
                LocalDateTime.of(2022, 10, 1, 12, 0),
                LocalDateTime.of(2022, 10, 2, 12, 0),
                item2.getId(), user1.getId(), Status.WAITING);
        booking3 = new Booking(3L,
                LocalDateTime.of(2022, 10, 2, 12, 0),
                LocalDateTime.of(2022, 10, 1, 12, 0),
                item2.getId(), user1.getId(), Status.REJECTED);
    }

    @Test
    void addBookingFailUserId() {
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> bookingService.addBooking(user1.getId(), booking2));
    }

    @Test
    void addBookingFailItemId() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> bookingService.addBooking(user1.getId(), booking2));
    }

    @Test
    void addBookingUnavailable() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item3));
        assertThrows(UnavailableException.class, () -> bookingService.addBooking(user1.getId(), booking2));
    }

    @Test
    void addBookingUnavailableForOwner() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        assertThrows(NotFoundException.class, () -> bookingService.addBooking(user1.getId(), booking2));
    }

    @Test
    void addIncorrectBooking() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item2));
        assertThrows(UnavailableException.class, () -> bookingService.addBooking(user1.getId(), booking3));
    }

    @Test
    void addBooking() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item2));
        when(bookingStorage.save(booking2))
                .thenReturn(booking2);
        final Booking savedBooking = bookingService.addBooking(user1.getId(), booking2);
        assertNotNull(savedBooking);
        assertEquals(booking2.getId(), savedBooking.getId());
        assertEquals(booking2.getStart(), savedBooking.getStart());
        assertEquals(booking2.getEnd(), savedBooking.getEnd());
        assertEquals(booking2.getStatus(), savedBooking.getStatus());
        assertEquals(booking2.getItemId(), savedBooking.getItemId());
        assertEquals(booking2.getBookerId(), savedBooking.getBookerId());

    }

    @Test
    void confirmBookingByFailUser() {
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> bookingService
                .confirmBookingByOwner(user2.getId(), booking2.getId(), true));
    }

    @Test
    void confirmBookingByFailOwner() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));
        when(bookingStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking2));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        assertThrows(NotFoundException.class, () -> bookingService
                .confirmBookingByOwner(user2.getId(), booking2.getId(), true));
    }

    @Test
    void confirmBookingByOwnerFailBooking() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));
        when(bookingStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> bookingService
                .confirmBookingByOwner(user2.getId(), booking2.getId(), true));
    }

    @Test
    void confirmBookingByOwnerFailBookingStatus() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));
        when(bookingStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item2));
        assertThrows(UnavailableException.class, () -> bookingService
                .confirmBookingByOwner(user2.getId(), booking2.getId(), true));
    }

    @Test
    void confirmBookingByOwnerApproved() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(bookingStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking2));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        final Booking confirmBooking = bookingService
                .confirmBookingByOwner(user1.getId(), booking2.getId(), true);
        assertNotNull(confirmBooking);
        assertEquals(booking2.getId(), confirmBooking.getId());
        assertEquals(booking2.getStart(), confirmBooking.getStart());
        assertEquals(booking2.getEnd(), confirmBooking.getEnd());
        assertEquals(booking2.getStatus(), confirmBooking.getStatus());
        assertEquals(booking2.getItemId(), confirmBooking.getItemId());
        assertEquals(booking2.getBookerId(), confirmBooking.getBookerId());
    }

    @Test
    void confirmBookingByOwnerRejected() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(bookingStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking3));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        final Booking confirmBooking = bookingService
                .confirmBookingByOwner(user1.getId(), booking2.getId(), false);
        assertNotNull(confirmBooking);
        assertEquals(booking3.getId(), confirmBooking.getId());
        assertEquals(booking3.getStart(), confirmBooking.getStart());
        assertEquals(booking3.getEnd(), confirmBooking.getEnd());
        assertEquals(booking3.getStatus(), confirmBooking.getStatus());
        assertEquals(booking3.getItemId(), confirmBooking.getItemId());
        assertEquals(booking3.getBookerId(), confirmBooking.getBookerId());
    }

    @Test
    void getBookingFailUserId() {
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(user1.getId(), booking1.getId()));
    }

    @Test
    void getBookingFailBookingId() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(bookingStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(user1.getId(), booking1.getId()));
    }

    @Test
    void getBookingFailOwner() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user3));
        when(bookingStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        assertThrows(NotFoundException.class, () -> bookingService.getBooking(1000L, booking1.getId()));
    }

    @Test
    void getBooking() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(bookingStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking2));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item2));
        final Booking booking = bookingService.getBooking(user1.getId(), booking1.getId());
        assertNotNull(booking);
        assertEquals(booking2.getId(), booking.getId());
        assertEquals(booking2.getStart(), booking.getStart());
        assertEquals(booking2.getEnd(), booking.getEnd());
        assertEquals(booking2.getStatus(), booking.getStatus());
        assertEquals(booking2.getItemId(), booking.getItemId());
        assertEquals(booking2.getBookerId(), booking.getBookerId());
    }

    @Test
    void getBookingsByBookerIdFailUserId() {
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> bookingService
                .getBookingsByBookerId(user1.getId(), "state", 0, 10));
    }

    @Test
    void getBookingsByBookerIdFailState() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        assertThrows(UnsupportedStatusException.class, () -> bookingService
                .getBookingsByBookerId(user1.getId(), "state", 0, 10));
    }

    @Test
    void getBookingsByBookerIdStateAll() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(bookingStorage.findAllBookingsByBookerId(user1.getId(), Pageable.ofSize(10)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByBookerId(user1.getId(), "ALL", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByBookerIdStatePast() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(bookingStorage
                .findAllPastBookingsByBookerId(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByBookerId(user1.getId(), "PAST", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByBookerIdStateFuture() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(bookingStorage
                .findAllFutureBookingsByBookerId(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByBookerId(user1.getId(), "FUTURE", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByBookerIdStateWaiting() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(bookingStorage
                .findByStatusAllBookingsByBookerId(anyLong(), any(Status.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByBookerId(user1.getId(), "WAITING", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByBookerIdStateRejected() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(bookingStorage
                .findByStatusAllBookingsByBookerId(anyLong(), any(Status.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByBookerId(user1.getId(), "REJECTED", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByBookerIdStateCurrent() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(bookingStorage
                .findAllCurrentBookingsByBookerId(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByBookerId(user1.getId(), "CURRENT", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByIdOwnerItemStateAll() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findAllItemByOwnerId(anyLong()))
                .thenReturn(List.of(item1));
        when(bookingStorage.findByIdOwnerItem(anyLong(), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByIdOwnerItem(user1.getId(), "ALL", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByIdOwnerItemState() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findAllItemByOwnerId(anyLong()))
                .thenReturn(List.of(item1));
        when(bookingStorage.findByIdOwnerItem(anyLong(), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByIdOwnerItem(user1.getId(), "ALL", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByIdOwnerItemStatePast() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findAllItemByOwnerId(anyLong()))
                .thenReturn(List.of(item1));
        when(bookingStorage
                .findAllPastBookingsByIdOwnerItem(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByIdOwnerItem(user1.getId(), "PAST", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByIdOwnerItemStateFuture() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findAllItemByOwnerId(anyLong()))
                .thenReturn(List.of(item1));
        when(bookingStorage
                .findAllFutureBookingsByIdOwnerItem(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByIdOwnerItem(user1.getId(), "FUTURE", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByIdOwnerItemStateWaiting() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findAllItemByOwnerId(anyLong()))
                .thenReturn(List.of(item1));
        when(bookingStorage
                .findByIdOwnerItemAndStatusIs(anyLong(), any(Status.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByIdOwnerItem(user1.getId(), "WAITING", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByIdOwnerItemStateRejected() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findAllItemByOwnerId(anyLong()))
                .thenReturn(List.of(item1));
        when(bookingStorage
                .findByIdOwnerItemAndStatusIs(anyLong(), any(Status.class), any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByIdOwnerItem(user1.getId(), "REJECTED", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByIdOwnerItemStateCurrent() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findAllItemByOwnerId(anyLong()))
                .thenReturn(List.of(item1));
        when(bookingStorage.findAllCurrentBookingByIdOwnerItem(anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService
                .getBookingsByIdOwnerItem(user1.getId(), "CURRENT", 0, 10);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void getBookingsByIdOwnerItemFailUserId() {
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> bookingService
                .getBookingsByIdOwnerItem(user1.getId(), "state", 0, 10));
    }

    @Test
    void getBookingsByIdOwnerItemFailState() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        assertThrows(UnsupportedStatusException.class, () -> bookingService
                .getBookingsByIdOwnerItem(user1.getId(), "state", 0, 10));
    }

    @Test
    void getBookingsByItemId() {
        when(bookingStorage.findByItemId(anyLong()))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService.getBookingsByItemId(user1.getId());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void findAllPastBookingsByItemId() {
        when(bookingStorage.findAllPastBookingsByItemId(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService.findAllPastBookingsByItemId(user1.getId(), LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }

    @Test
    void findAllFutureBookingsByItemId() {
        when(bookingStorage.findAllFutureBookingsByItemId(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking1));
        final List<Booking> bookings = bookingService.findAllFutureBookingsByItemId(user1.getId(), LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
    }
}