package ru.practicum.shareit.item.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.requests.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
public class ItemDto {
    Long id;
    @NotBlank(message = "Нет названия вещи")
    String name;
    @NotBlank(message = "Нет описания вещи")
    String description;
    @NotNull(message = "Нет статуса аренды")
    Boolean available;
    Long owner;
    ItemRequest request;
}
