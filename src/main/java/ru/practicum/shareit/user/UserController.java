package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.InputUserDto;
import ru.practicum.shareit.user.dto.OutputUserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public OutputUserDto addUser(@Valid @RequestBody InputUserDto inputUserDto) {
        User user = userMapper.toUser(inputUserDto);
        return userMapper.toOutputUserDto(userService.addUser(user));
    }

    @PatchMapping(value = "/{id}")
    public OutputUserDto updateUser(@PathVariable Long id, @RequestBody InputUserDto inputUserDto) {
        User user = userMapper.toUser(inputUserDto);
        return userMapper.toOutputUserDto(userService.updateUser(id, user));
    }

    @GetMapping(value = "/{id}")
    public OutputUserDto getUser(@PathVariable Long id) {
        return userMapper.toOutputUserDto(userService.getUser(id));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<OutputUserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(userMapper::toOutputUserDto)
                .collect(Collectors.toList());
    }
}
