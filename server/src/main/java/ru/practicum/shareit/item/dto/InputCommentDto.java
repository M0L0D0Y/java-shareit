package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class InputCommentDto {
    private String text;

    public InputCommentDto() {
    }

    public InputCommentDto(String text) {
        this.text = text;
    }
}

