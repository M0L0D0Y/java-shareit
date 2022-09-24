package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UpdateUser {
    private String name;
    @Email
    private String email;

    public UpdateUser(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
