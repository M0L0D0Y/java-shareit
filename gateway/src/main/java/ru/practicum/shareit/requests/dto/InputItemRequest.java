package ru.practicum.shareit.requests.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class InputItemRequest {
    @NotBlank(message = "Нет описания требуемой вещи")
    String description;

    public InputItemRequest(String description) {
        this.description = description;
    }
}
