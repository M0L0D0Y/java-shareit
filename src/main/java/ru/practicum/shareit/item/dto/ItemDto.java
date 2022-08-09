package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * // TODO .
 */
@Data
public class ItemDto {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    boolean available;
    Long owner;
    Long request;
}
