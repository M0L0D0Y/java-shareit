package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.UnavailableException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    UserService userService;
    UserStorage userStorage;
    User user1;
    User user2;

    @BeforeEach
    void beforeEach() {
        userStorage = mock(UserStorage.class);
        userService = new UserServiceImpl(userStorage);
        user1 = new User("user1", "user1@mail.ru");
        user1.setId(1L);
        user2 = new User("user2", "user2@mail.ru");
        user2.setId(2L);
    }

    @AfterEach
    void afterEach() {
        userStorage.deleteAll();
    }

    @Test
    void updateUser() {
        User user = new User();
        user.setId(user1.getId());
        user.setName("update");
        user.setEmail("update@email.ru");
        when(userStorage.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user1));
        when(userStorage.save(user))
                .thenReturn(user);

        final User updateUser = userService.updateUser(user.getId(), user);
        assertNotNull(updateUser);
        assertEquals(user.getId(), updateUser.getId());
        assertEquals(user.getName(), updateUser.getName());
        assertEquals(user.getEmail(), updateUser.getEmail());
    }

    @Test
    void updateUserNullName() {
        User user = new User();
        user.setId(user1.getId());
        user.setEmail("update@email.ru");
        when(userStorage.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user1));
        when(userStorage.save(user))
                .thenReturn(user);

        final User updateUser = userService.updateUser(user.getId(), user);
        assertNotNull(updateUser);
        assertEquals(user.getId(), updateUser.getId());
        assertEquals(user.getName(), updateUser.getName());
        assertEquals(user.getEmail(), updateUser.getEmail());
    }

    @Test
    void updateUserNullEmail() {
        User user = new User();
        user.setId(user1.getId());
        user.setName("update");
        when(userStorage.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user1));
        when(userStorage.save(user))
                .thenReturn(user);

        final User updateUser = userService.updateUser(user.getId(), user);
        assertNotNull(updateUser);
        assertEquals(user.getId(), updateUser.getId());
        assertEquals(user.getName(), updateUser.getName());
        assertEquals(user.getEmail(), updateUser.getEmail());
    }

    @Test
    void updateUserNull() {
        User user = new User();
        user.setId(user1.getId());
        when(userStorage.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(user1));

        assertThrows(UnavailableException.class, () -> userService.updateUser(user.getId(), user),
                "Нет данных для обновления!");
    }

}