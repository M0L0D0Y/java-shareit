package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class CommentStorageTest {

    @Autowired
    CommentStorage commentStorage;
    @Autowired
    UserStorage userStorage;
    @Autowired
    ItemStorage itemStorage;
    User user;
    Item item;
    Comment comment;

    @BeforeEach
    void beforeEach() {
        user = userStorage.save(new User(1L, "user", "user@mail.ru"));
        item = itemStorage.save(
                new Item(1L, "item", "description", true, user, null));
        comment = new Comment(1L, "comment", item.getId(), user.getId(), LocalDateTime.now());
        commentStorage.save(comment);
    }

    @AfterEach
    void afterEach() {
        commentStorage.deleteAll();
    }

    @Test
    void findByFalseItemId() {
        long falseItemId = 1000L;
        final List<Comment> comments = commentStorage.findByItemIdOrderById(falseItemId);
        assertNotNull(comments);
        assertEquals(0, comments.size());
    }

    @Test
    void findByItemId() {
        final List<Comment> comments = commentStorage.findByItemIdOrderById(item.getId());
        assertNotNull(comments);
        assertEquals(1, comments.size());
    }
}