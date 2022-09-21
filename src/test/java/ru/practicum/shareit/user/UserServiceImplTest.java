package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.exception.UserValidationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    UserService userService;
    UserStorage userStorage;
    User user;

    @BeforeEach
    void beforeEach() {
        userStorage = mock(UserStorage.class);
        userService = new UserServiceImpl(userStorage);
        user = new User(1L, "user1", "user1@mail.ru");
    }

    @Test
    void addUserNullName() {
        when(userStorage.save(any(User.class)))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.addUser(user));
    }

    @Test
    void addUserNullEmail() {
        when(userStorage.save(any(User.class)))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.addUser(user));
    }

    @Test
    void addUserDuplicateEmail() {
        when(userStorage.save(any(User.class)))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.addUser(user));
    }

    @Test
    void addUser() {
        when(userStorage.save(any(User.class)))
                .thenReturn(user);
        final User savedUser = userService.addUser(user);
        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getEmail(), savedUser.getEmail());
    }

    @Test
    void getAllUsers() {
        when(userStorage.findAll())
                .thenReturn(List.of(user));
        final List<User> foundUsers = userService.getAllUsers();
        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        assertEquals(user.getId(), foundUsers.get(0).getId());
        assertEquals(user.getName(), foundUsers.get(0).getName());
        assertEquals(user.getEmail(), foundUsers.get(0).getEmail());
    }

    @Test
    void getUserFailId() {
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> userService.getUser(anyLong()));
    }

    @Test
    void getUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        final User foundUser = userService.getUser(user.getId());
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void updateUser() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(userStorage.save(any(User.class)))
                .thenReturn(user);

        final User updateUser = userService.updateUser(user.getId(), user);
        assertNotNull(updateUser);
        assertEquals(user.getId(), updateUser.getId());
        assertEquals(user.getName(), updateUser.getName());
        assertEquals(user.getEmail(), updateUser.getEmail());
    }

    @Test
    void updateUserFailUserId() {
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> userService.updateUser(user.getId(), user));
    }

    @Test
    void updateUserDuplicateEmail() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(userStorage.findAll())
                .thenReturn(List.of(user));

        assertThrows(UserValidationException.class, () -> userService.updateUser(user.getId(), user));
    }

    @Test
    void updateUserNullName() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(userStorage.save(any(User.class)))
                .thenReturn(user);

        final User updateUser = userService.updateUser(user.getId(), user);
        assertNotNull(updateUser);
        assertEquals(user.getId(), updateUser.getId());
        assertEquals(user.getName(), updateUser.getName());
        assertEquals(user.getEmail(), updateUser.getEmail());
    }

    @Test
    void updateUserNullEmail() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(userStorage.save(any(User.class)))
                .thenReturn(user);

        final User updateUser = userService.updateUser(user.getId(), user);
        assertNotNull(updateUser);
        assertEquals(user.getId(), updateUser.getId());
        assertEquals(user.getName(), updateUser.getName());
        assertEquals(user.getEmail(), updateUser.getEmail());
    }

    @Test
    void updateUserNull() {
        user.setEmail(null);
        user.setName(null);
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        assertThrows(UnavailableException.class, () -> userService.updateUser(user.getId(), user),
                "Нет данных для обновления!");
    }

    @Test
    void deleteUserFailId() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(anyLong()));
    }

}