package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validate.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InputItem {
    @NotBlank(groups = {Create.class}, message = "Нет названия вещи")
    private String name;
    @NotBlank(groups = {Create.class},message = "Нет описания вещи")
    private String description;
    @NotNull(groups = {Create.class},message = "Нет статуса аренды")
    private Boolean available;
    private Long requestId;

    public InputItem(String name, String description, Boolean available, Long requestId) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}
