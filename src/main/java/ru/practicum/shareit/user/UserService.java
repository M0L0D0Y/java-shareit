package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User getUser(long id);

    User addUser(User user);

    User updateUser(long id, User user);

    void deleteUser(long id);

    List<User> getAllUsers();
}
