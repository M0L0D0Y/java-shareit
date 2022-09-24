package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class InputUserDto {
    private String name;
    private String email;

    public InputUserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
