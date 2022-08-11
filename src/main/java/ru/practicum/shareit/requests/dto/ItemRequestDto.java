package ru.practicum.shareit.requests.dto;

import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ItemRequestDto {
    Long id;
    @NotBlank(message = "Нет описания требуемой вещи")
    String description;
    @NotNull(message = "Не указан пользователь создавший запрос")
    User requestor;
    @NotNull(message = "Не указана дата создания запроса")
    LocalDateTime created;
}
