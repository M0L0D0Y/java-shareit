package ru.practicum.shareit.requests.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InputItemRequestDto {
    String description;

    public InputItemRequestDto(String description) {
        this.description = description;
    }
}
