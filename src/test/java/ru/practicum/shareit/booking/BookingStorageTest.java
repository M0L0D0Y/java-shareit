package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BookingStorageTest {
    @Autowired
    BookingStorage bookingStorage;
    @Autowired
    ItemStorage itemStorage;
    @Autowired
    UserStorage userStorage;
    User user1;
    User user2;
    Item item1;
    Item item2;
    Booking booking1;
    Booking booking2;

    @BeforeEach
    void beforeEach() {
        user1 = userStorage.save(new User(1L, "user1", "user1@mail.ru"));
        item1 = itemStorage.save(
                new Item(1L, "item1", "description1", true, user1, null));
        user2 = userStorage.save(new User(2L, "user2", "user2@mail.ru"));
        item2 = itemStorage.save(
                new Item(2L, "item2", "description2",
                        true, user2, null));
        booking1 = bookingStorage.save(new Booking(1L,
                LocalDateTime.of(2022, 8, 10, 12, 0),
                LocalDateTime.of(2022, 8, 11, 12, 0),
                item1.getId(), user2.getId(), Status.WAITING));
        booking2 = bookingStorage.save(new Booking(2L,
                LocalDateTime.of(2022, 10, 10, 12, 0),
                LocalDateTime.of(2022, 10, 11, 12, 0),
                item2.getId(), user1.getId(), Status.APPROVED));
    }

    @AfterEach
    void afterEach() {
        bookingStorage.deleteAll();
        itemStorage.deleteAll();
        userStorage.deleteAll();
    }

    @Test
    void findAllBookingsByBookerIdEmptyList() {
        final List<Booking> bookings = bookingStorage.findAllBookingsByBookerId(100L, Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findAllBookingsByBookerId() {
        final List<Booking> bookings = bookingStorage
                .findAllBookingsByBookerId(user1.getId(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking2.getId(), bookings.get(0).getId());
        assertEquals(booking2.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking2.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking2.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking2.getStart(), bookings.get(0).getStart());
        assertEquals(booking2.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findAllFutureBookingsByBookerIdEmptyList() {
        final List<Booking> bookings = bookingStorage
                .findAllFutureBookingsByBookerId(user2.getId(), LocalDateTime.now(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findAllFutureBookingsByBookerId() {
        final List<Booking> bookings = bookingStorage
                .findAllFutureBookingsByBookerId(user1.getId(), LocalDateTime.now(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking2.getId(), bookings.get(0).getId());
        assertEquals(booking2.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking2.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking2.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking2.getStart(), bookings.get(0).getStart());
        assertEquals(booking2.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findAllPastBookingsByBookerIdEmptyList() {
        final List<Booking> bookings = bookingStorage
                .findAllPastBookingsByBookerId(user1.getId(), LocalDateTime.now(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findAllPastBookingsByBookerId() {
        final List<Booking> bookings = bookingStorage
                .findAllPastBookingsByBookerId(user2.getId(), LocalDateTime.now(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findByStatusWaitingAllBookingsByBookerId() {
        final List<Booking> bookings = bookingStorage
                .findByStatusAllBookingsByBookerId(user1.getId(), Status.WAITING, Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findByStatusRejectedAllBookingsByBookerId() {
        final List<Booking> bookings = bookingStorage
                .findByStatusAllBookingsByBookerId(user1.getId(), Status.REJECTED, Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findByStatusCanceledAllBookingsByBookerId() {
        final List<Booking> bookings = bookingStorage
                .findByStatusAllBookingsByBookerId(user1.getId(), Status.CANCELED, Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findByStatusApprovedAllBookingsByBookerId() {
        final List<Booking> bookings = bookingStorage
                .findByStatusAllBookingsByBookerId(user1.getId(), Status.APPROVED, Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking2.getId(), bookings.get(0).getId());
        assertEquals(booking2.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking2.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking2.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking2.getStart(), bookings.get(0).getStart());
        assertEquals(booking2.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findAllCurrentBookingsByFalseBookerId() {
        final List<Booking> bookings = bookingStorage
                .findAllCurrentBookingsByBookerId(100L,
                        LocalDateTime.of(2022, 10, 10, 15, 0),
                        Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findAllCurrentBookingsByBookerIdEmptyList() {
        final List<Booking> bookings = bookingStorage
                .findAllCurrentBookingsByBookerId(user1.getId(), LocalDateTime.now(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findAllCurrentBookingsByBookerId() {
        final List<Booking> bookings = bookingStorage
                .findAllCurrentBookingsByBookerId(user1.getId(),
                        LocalDateTime.of(2022, 10, 10, 15, 0),
                        Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking2.getId(), bookings.get(0).getId());
        assertEquals(booking2.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking2.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking2.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking2.getStart(), bookings.get(0).getStart());
        assertEquals(booking2.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findByIdOwnerItemEmptyList() {
        final List<Booking> bookings = bookingStorage.findByIdOwnerItem(100L, Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findByIdOwnerItem() {
        final List<Booking> bookings = bookingStorage
                .findByIdOwnerItem(user1.getId(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findAllPastBookingsByIdOwnerItemEmptyList() {
        final List<Booking> bookings = bookingStorage
                .findAllPastBookingsByIdOwnerItem(user2.getId(), LocalDateTime.now(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findAllPastBookingsByIdOwnerItem() {
        final List<Booking> bookings = bookingStorage
                .findAllPastBookingsByIdOwnerItem(user1.getId(), LocalDateTime.now(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findAllFutureBookingsByIdOwnerItemEmptyList() {
        final List<Booking> bookings = bookingStorage
                .findAllFutureBookingsByIdOwnerItem(user1.getId(), LocalDateTime.now(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());

    }

    @Test
    void findAllFutureBookingsByIdOwnerItem() {
        final List<Booking> bookings = bookingStorage
                .findAllFutureBookingsByIdOwnerItem(user2.getId(), LocalDateTime.now(), Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking2.getId(), bookings.get(0).getId());
        assertEquals(booking2.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking2.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking2.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking2.getStart(), bookings.get(0).getStart());
        assertEquals(booking2.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findByIdOwnerItemAndStatusIsApproved() {
        final List<Booking> bookings = bookingStorage
                .findByIdOwnerItemAndStatusIs(user2.getId(), Status.APPROVED, Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking2.getId(), bookings.get(0).getId());
        assertEquals(booking2.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking2.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking2.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking2.getStart(), bookings.get(0).getStart());
        assertEquals(booking2.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findByIdOwnerItemAndStatusIsWaiting() {
        final List<Booking> bookings = bookingStorage
                .findByIdOwnerItemAndStatusIs(user1.getId(), Status.WAITING, Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findByIdOwnerItemAndStatusIsRejected() {
        final List<Booking> bookings = bookingStorage
                .findByIdOwnerItemAndStatusIs(user1.getId(), Status.REJECTED, Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findByIdOwnerItemAndStatusIsCanceled() {
        final List<Booking> bookings = bookingStorage
                .findByIdOwnerItemAndStatusIs(user1.getId(), Status.CANCELED, Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findAllCurrentBookingByIdOwnerItemEmptyLis() {
        final List<Booking> bookings = bookingStorage
                .findAllCurrentBookingByIdOwnerItem(user1.getId(),
                        LocalDateTime.now(),
                        Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findAllCurrentBookingByIdOwnerItem() {
        final List<Booking> bookings = bookingStorage
                .findAllCurrentBookingByIdOwnerItem(user1.getId(),
                        LocalDateTime.of(2022, 8, 10, 15, 0),
                        Pageable.ofSize(10));
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findByItemIdEmptyList() {
        final List<Booking> bookings = bookingStorage.findByItemId(1000L);
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findByItemId() {
        final List<Booking> bookings = bookingStorage.findByItemId(item1.getId());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findAllFutureBookingsByItemId() {
        final List<Booking> bookings = bookingStorage
                .findAllFutureBookingsByItemId(item2.getId(),
                        LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking2.getId(), bookings.get(0).getId());
        assertEquals(booking2.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking2.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking2.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking2.getStart(), bookings.get(0).getStart());
        assertEquals(booking2.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findAllFutureBookingsByItemIdEmptyList() {
        final List<Booking> bookings = bookingStorage
                .findAllFutureBookingsByItemId(item1.getId(),
                        LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findAllPastBookingsByItemId() {
        final List<Booking> bookings = bookingStorage
                .findAllPastBookingsByItemId(item1.getId(),
                        LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findAllPastBookingsByItemIdEmptyLst() {
        final List<Booking> bookings = bookingStorage
                .findAllPastBookingsByItemId(item2.getId(),
                        LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void findAllPastBookingsByBookerAndItemId() {
        final List<Booking> bookings = bookingStorage
                .findAllPastBookingsByBookerAndItemId(item1.getId(),
                        user2.getId(),
                        LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
        assertEquals(booking1.getBookerId(), bookings.get(0).getBookerId());
        assertEquals(booking1.getItemId(), bookings.get(0).getItemId());
        assertEquals(booking1.getStatus(), bookings.get(0).getStatus());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
        assertEquals(booking1.getEnd(), bookings.get(0).getEnd());
    }

    @Test
    void findAllPastBookingsByBookerAndItemIdEmptyList() {
        final List<Booking> bookings = bookingStorage
                .findAllPastBookingsByBookerAndItemId(item2.getId(),
                        user1.getId(),
                        LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }
}