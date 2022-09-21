package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
public class OutputItemDtoWithComment {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<OutputCommentDto> comments;

    public OutputItemDtoWithComment() {
    }

    public OutputItemDtoWithComment(Long id,
                                    String name,
                                    String description,
                                    Boolean available,
                                    BookingDto lastBooking,
                                    BookingDto nextBooking,
                                    List<OutputCommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = comments;
    }
}
