package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

@Data
public class OutputItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private Long requestId;

    public OutputItemDto() {
    }

    public OutputItemDto(Long id,
                         String name,
                         String description,
                         Boolean available,
                         BookingDto lastBooking,
                         BookingDto nextBooking,
                         Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.requestId = requestId;
    }
}
