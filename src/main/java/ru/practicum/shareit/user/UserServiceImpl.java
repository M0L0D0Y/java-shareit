package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserValidationException;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User getUser(long userId) {
        User user = userStorage.findById(userId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет " + userId));
        log.info("Пользователь с id = {} получен", userId);
        return user;
    }

    @Override
    public User addUser(User user) {
        User savedUser = userStorage.save(user);
        log.info("Пользователь сохранен");
        return savedUser;
    }

    @Override
    public User updateUser(long userId, User user) {
        User userUpdate = userStorage.findById(userId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет " + userId));
        log.info("Пользователь для обновления найден по id = {}", userId);
        if (user.getName() != null) {
            userUpdate.setName(user.getName());
            log.info("Обновили имя");
        }
        if (user.getEmail() != null) {
            List<User> userList = userStorage.findAll();
            if (!(userList.isEmpty())) {
                for (User userValid : userList) {
                    if (user.getEmail().equals(userValid.getEmail())) {
                        throw new UserValidationException("Пользователь с такой почтой уже есть " + user.getEmail());
                    }
                }
            }
            userUpdate.setEmail(user.getEmail());
            log.info("Обновили email");
        }
        User savedUserUpdate = userStorage.save(userUpdate);
        log.info("Пользователь обновлен");
        return savedUserUpdate;
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.findById(userId)
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет " + userId));
        userStorage.deleteById(userId);
        log.info("Пользоватеь с id = {} удален", userId);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userStorage.findAll();
        log.info("Все пользователи получены");
        return users;
    }
}
