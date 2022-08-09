package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.ecxeption.UserValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserIdGenerator generator;
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserIdGenerator generator, UserStorage userStorage, UserMapper userMapper) {
        this.generator = generator;
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto getUser(long id) {
        return userMapper.toUserDto(userStorage.getUser(id));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        List<User> userList = userStorage.getAllUser();
        for (User userValid : userList) {
            if (user.getEmail().equals(userValid.getEmail())) {
                throw new UserValidException("Пользователь с такой почтой уже есть" + user.getEmail());
            }
        }
        user.setId(generator.getId());
        return userMapper.toUserDto(userStorage.addUser(user));
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User userUpdate = userStorage.getUser(id);
        String name = user.getName();
        String email = user.getEmail();
        if (name != null) {
            userUpdate.setName(user.getName());
        }
        if (email != null) {
            List<User> userList = userStorage.getAllUser();
            if (!(userList.isEmpty())) {
                for (User userValid : userList) {
                    if (user.getEmail().equals(userValid.getEmail())) {
                        throw new UserValidException("Пользователь с такой почтой уже есть" + user.getEmail());
                    }
                }
            }
            userUpdate.setEmail(user.getEmail());
        }
        return userMapper.toUserDto(userStorage.updateUser(userUpdate));
    }

    @Override
    public void deleteUser(long id) {
        userStorage.deleteUser(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userStorage.getAllUser();
        List<UserDto> userDtoList = new LinkedList<>();
        for (User user : userList) {
            UserDto userDto = userMapper.toUserDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

}
