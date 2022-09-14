package ru.practicum.shareit.requests;

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
class ItemRequestStorageTest {
    @Autowired
    UserStorage userStorage;
    @Autowired
    ItemStorage itemStorage;
    @Autowired
    ItemRequestStorage itemRequestStorage;

    User user1;
    User user2;
    Item item1;
    ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        user1 = userStorage.save(new User(1L, "user1", "user1@mail.ru"));
        item1 = itemStorage.save(
                new Item(1L, "item1", "description1", true, user1.getId(), null));
        itemRequest = itemRequestStorage.save(
                new ItemRequest(1L, "description", user1.getId(), LocalDateTime.now()));
        user2 = userStorage.save(new User(2L, "user2", "user2@mail.ru"));
    }

    @AfterEach
    void afterEach() {
        itemRequestStorage.deleteAll();
        itemStorage.deleteAll();
        userStorage.deleteAll();
    }

    @Test
    void getAllMeRequestFalseUserId() {
        final List<ItemRequest> itemRequests = itemRequestStorage.getAllMeRequest(1000L);
        assertNotNull(itemRequests);
        assertEquals(0, itemRequests.size());
    }

    @Test
    void getAllMeRequest() {
        final List<ItemRequest> itemRequests = itemRequestStorage.getAllMeRequest(user1.getId());
        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
    }

    @Test
    void getAllRequestExceptMe() {
        final List<ItemRequest> itemRequests = itemRequestStorage
                .getAllRequest(user1.getId(), Pageable.ofSize(10));
        assertNotNull(itemRequests);
        assertEquals(0, itemRequests.size());
    }

    @Test
    void getAllRequest() {
        final List<ItemRequest> itemRequests = itemRequestStorage
                .getAllRequest(user2.getId(), Pageable.ofSize(10));
        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
    }
}