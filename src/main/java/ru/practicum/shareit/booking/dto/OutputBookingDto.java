package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.OutputItemDto;
import ru.practicum.shareit.user.dto.OutputUserDto;

import java.time.LocalDateTime;

@Data
public class OutputBookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private OutputItemDto item;
    private OutputUserDto booker;
    private Status status;

    public OutputBookingDto() {
    }

    public OutputBookingDto(Long id,
                            LocalDateTime start,
                            LocalDateTime end,
                            OutputItemDto item,
                            OutputUserDto booker,
                            Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}
