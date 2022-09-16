package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.InputUserDto;
import ru.practicum.shareit.user.dto.OutputUserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    UserMapper userMapper = new UserMapper();
    User user;
    InputUserDto inputUserDto;
    OutputUserDto outputUserDto;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "user", "user@mail.ru");
        inputUserDto = new InputUserDto("user", "user@mail.ru");
        outputUserDto = new OutputUserDto(1L, "user", "user@mail.ru");
    }

    @Test
    void toOutputUserDto() {
        final OutputUserDto dto = userMapper.toOutputUserDto(user);
        assertEquals(outputUserDto.getId(), dto.getId());
        assertEquals(outputUserDto.getName(), dto.getName());
        assertEquals(outputUserDto.getEmail(), dto.getEmail());
    }

    @Test
    void toUser() {
        final User user1 = userMapper.toUser(inputUserDto);
        assertEquals(user.getName(), user1.getName());
        assertEquals(user.getEmail(), user1.getEmail());
    }
}