package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemStorageTest {
    @Autowired
    UserStorage userStorage;
    @Autowired
    ItemStorage itemStorage;
    @Autowired
    ItemRequestStorage itemRequestStorage;
    User user1;
    User user2;
    Item item1;
    Item item2;
    ItemRequest itemRequest;


    //TODO добавить начальные данные и удаление данных для всех тестов в таком виде
    @BeforeEach
    void beforeEach() {
        user1 = userStorage.save(new User("user1", "user1@mail.ru"));
        item1 = itemStorage.save(
                new Item("item1", "description1", true, user1.getId(), null));
        itemRequest = itemRequestStorage.save(
                new ItemRequest("description", user1.getId(), LocalDateTime.now()));
        user2 = userStorage.save(new User("user2", "user2@mail.ru"));
        item2 = itemStorage.save(
                new Item("item2", "description2", true, user2.getId(), itemRequest.getId()));

    }

    @AfterEach
    void afterEach() {
        itemRequestStorage.deleteAll();
        itemStorage.deleteAll();
        userStorage.deleteAll();
    }

    @Test
    void findItemByOwnerId() {
        final List<Item> byOwner = itemStorage.findItemByOwnerId(user1.getId(), Pageable.unpaged());
        assertNotNull(byOwner);
        assertEquals(1, byOwner.size());
        Item foundItem = byOwner.get(0);
        assertEquals(item1.getId(), foundItem.getId());
        assertEquals(item1.getOwnerId(), foundItem.getOwnerId());
        assertEquals(item1.getAvailable(), foundItem.getAvailable());
        assertEquals(item1.getName(), foundItem.getName());
        assertEquals(item1.getDescription(), foundItem.getDescription());
        assertEquals(item1.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void findAllItemByOwnerId() {
        final List<Item> byOwner = itemStorage.findAllItemByOwnerId(user1.getId());
        assertNotNull(byOwner);
        assertEquals(1, byOwner.size());
        Item foundItem = byOwner.get(0);
        assertEquals(item1.getId(), foundItem.getId());
        assertEquals(item1.getOwnerId(), foundItem.getOwnerId());
        assertEquals(item1.getAvailable(), foundItem.getAvailable());
        assertEquals(item1.getName(), foundItem.getName());
        assertEquals(item1.getDescription(), foundItem.getDescription());
        assertEquals(item1.getRequestId(), foundItem.getRequestId());

    }

    @Test
    void searchItemByText() {
        final List<Item> byText = itemStorage.searchItemByText("Item1", Pageable.unpaged());
        assertNotNull(byText);
        assertEquals(1, byText.size());
        Item foundItem = byText.get(0);
        assertEquals(item1.getId(), foundItem.getId());
        assertEquals(item1.getOwnerId(), foundItem.getOwnerId());
        assertEquals(item1.getAvailable(), foundItem.getAvailable());
        assertEquals(item1.getName(), foundItem.getName());
        assertEquals(item1.getDescription(), foundItem.getDescription());
        assertEquals(item1.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void getAllItemByRequestId() {
        final List<Item> byRequestId = itemStorage.getAllItemByRequestId(1L);
        assertNotNull(byRequestId);
        assertEquals(1, byRequestId.size());
        Item foundItem = byRequestId.get(0);
        assertEquals(item2.getId(), foundItem.getId());
        assertEquals(item2.getOwnerId(), foundItem.getOwnerId());
        assertEquals(item2.getAvailable(), foundItem.getAvailable());
        assertEquals(item2.getName(), foundItem.getName());
        assertEquals(item2.getDescription(), foundItem.getDescription());
        assertEquals(item2.getRequestId(), foundItem.getRequestId());
    }
}