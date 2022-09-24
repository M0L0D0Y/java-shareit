package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public class UserMapper {
    public OutputUserDto toOutputUserDto(User user) {
        OutputUserDto outputUserDto = new OutputUserDto();
        outputUserDto.setId(user.getId());
        outputUserDto.setName(user.getName());
        outputUserDto.setEmail(user.getEmail());
        return outputUserDto;
    }

    public User toUser(InputUserDto inputUserDto) {
        User user = new User();
        user.setName(inputUserDto.getName());
        user.setEmail(inputUserDto.getEmail());
        return user;
    }
}
