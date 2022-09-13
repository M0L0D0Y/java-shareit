package ru.practicum.shareit.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
    void addUserNullName() {
        User user = new User();
        user.setEmail("user@mail.com");
        when(userStorage.save(user))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.addUser(user));
    }

    @Test
    void addUserNullEmail() {
        User user = new User();
        user.setName("user");
        when(userStorage.save(user))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.addUser(user));
    }

    @Test
    void addUserDuplicateEmail() {
        User user = new User("user3", "user1@mail.ru");
        when(userStorage.save(user))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.addUser(user));
    }

    @Test
    void addUser() {
        User user = new User("user3", "user3@mail.ru");
        when(userStorage.save(user))
                .thenReturn(user);
        final User savedUser = userService.addUser(user);
        assertNotNull(savedUser);
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void getUserFailId() {
        long failId = 1000L;
        when(userStorage.findById(failId))
                .thenThrow(new NotFoundException("Пользователя с таким id нет " + failId));
        assertThrows(NotFoundException.class, () -> userService.getUser(failId));
    }

    @Test
    void getAllUsers() {
        when(userStorage.findAll())
                .thenReturn(List.of(user1, user2));
        final List<User> foundUsers = userService.getAllUsers();
        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
        assertEquals(user1.getId(), foundUsers.get(0).getId());
        assertEquals(user1.getName(), foundUsers.get(0).getName());
        assertEquals(user1.getEmail(), foundUsers.get(0).getEmail());
        assertEquals(user2.getId(), foundUsers.get(1).getId());
        assertEquals(user2.getName(), foundUsers.get(1).getName());
        assertEquals(user2.getEmail(), foundUsers.get(1).getEmail());
    }

    @Test
    void getUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        final User foundUser = userService.getUser(user1.getId());
        assertNotNull(foundUser);
        assertEquals(user1.getId(), foundUser.getId());
        assertEquals(user1.getName(), foundUser.getName());
        assertEquals(user1.getEmail(), foundUser.getEmail());
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

    @Test
    void deleteUserFailId() {
        long failId = 1000L;
        assertThrows(NotFoundException.class, () -> userService.deleteUser(failId));
    }

}