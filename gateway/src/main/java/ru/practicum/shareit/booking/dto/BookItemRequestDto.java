package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
	@NotNull
	private long itemId;
	@FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
	private LocalDateTime start;
	@Future(message = "Дата окончания бронирования не может быть в прошлом")
	private LocalDateTime end;
}
