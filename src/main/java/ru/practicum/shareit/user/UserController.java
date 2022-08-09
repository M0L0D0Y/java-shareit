package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;

        this.userMapper = userMapper;
    }


    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userService.addUser(user));
    }

    @PatchMapping(value = "/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userService.updateUser(id, user));
    }

    @GetMapping(value = "/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userMapper.toUserDto(userService.getUser(id));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserDto> userDtoList = new LinkedList<>();
        for (User user : userList) {
            UserDto userDto = userMapper.toUserDto(user);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }
}
