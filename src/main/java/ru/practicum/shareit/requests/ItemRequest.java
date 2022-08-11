package ru.practicum.shareit.requests;

import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    Long id;
    String description;
    User requestor;
    LocalDateTime created;

}
