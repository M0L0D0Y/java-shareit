package ru.practicum.shareit.requests.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data

public class InputItemRequestDto {
    @NotBlank(message = "Нет описания требуемой вещи")
    String description;
}
