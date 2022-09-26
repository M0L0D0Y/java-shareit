package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.validate.Create;
import ru.practicum.shareit.validate.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class InputUser {
    @NotBlank(groups = {Create.class}, message = "Нет имени пользователя")
    private String name;
    @Email(groups = {Update.class, Create.class})
    @NotBlank(groups = {Create.class}, message = "Нет email пользователя")
    private String email;

    public InputUser(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
