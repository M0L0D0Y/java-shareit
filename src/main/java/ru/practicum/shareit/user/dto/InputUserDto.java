package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class InputUserDto {
    @NotBlank(message = "Нет имени пользователя")
    private String name;
    @NotBlank(message = "Нет email пользователя")
    @Email
    private String email;

    public InputUserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
