package ru.practicum.shareit.booking.dto;

import lombok.Data;

@Data
public class BookingDto {
    private Long id;
    private Long bookerId;

    public BookingDto() {
    }

    public BookingDto(Long id, Long bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}
