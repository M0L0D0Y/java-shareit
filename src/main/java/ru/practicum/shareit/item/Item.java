package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {
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
