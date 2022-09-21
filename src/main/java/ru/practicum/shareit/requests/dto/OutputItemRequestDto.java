package ru.practicum.shareit.requests.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.OutputItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OutputItemRequestDto {
    Long id;
    String description;
    LocalDateTime created;
    List<OutputItemDto> items;

    public OutputItemRequestDto() {
    }

    public OutputItemRequestDto(Long id, String description, LocalDateTime created, List<OutputItemDto> items) {
        this.id = id;
        this.description = description;
        this.created = created;
        this.items = items;
    }
}
