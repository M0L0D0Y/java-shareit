package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InputBookingDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;

    public InputBookingDto() {
    }

    public InputBookingDto(LocalDateTime start, LocalDateTime end, Long itemId) {
        this.start = start;
        this.end = end;
        this.itemId = itemId;
    }
}
