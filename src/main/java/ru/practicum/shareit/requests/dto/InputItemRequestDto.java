package ru.practicum.shareit.requests.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class InputItemRequestDto {
    @NotBlank(message = "Нет описания требуемой вещи")
    String description;

    public InputItemRequestDto(String description) {
        this.description = description;
    }
}
