package ru.practicum.shareit.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    Long id;
    @NotBlank
    String description;
    Long requestor;
    LocalDateTime created;

}
