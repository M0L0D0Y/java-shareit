package ru.practicum.shareit.booking.dto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BookingDto {
    Long id;
    @NotNull(message = "Не указана дата начала бронирования")
    @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
    LocalDateTime start;
    @NotNull(message = "Не указана дата окончания бронирования")
    @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
    LocalDateTime end;
    @NotNull(message = "Не указан пользователь, который бронирует вещь")
    Long itemId;
    @NotNull(message = "Не указана вещь, которую бронирует пользователь")
    Long bookerId;
    @NotBlank(message = "Не указан статус бронирования")
    String status;
}
