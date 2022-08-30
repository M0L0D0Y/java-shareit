package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.LastBooking;
import ru.practicum.shareit.booking.NextBooking;

import java.util.List;

@Data
public class OutputItemDtoWithComment {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private LastBooking lastBooking;
    private NextBooking nextBooking;
    private List<OutputCommentDto> comments;

}
