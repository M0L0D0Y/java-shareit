package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class OutputUserDto {
    private Long id;
    private String name;
    private String email;

    public OutputUserDto() {
    }

    public OutputUserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
