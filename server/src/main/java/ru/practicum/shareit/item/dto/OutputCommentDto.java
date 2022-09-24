package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OutputCommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;

    public OutputCommentDto() {
    }

    public OutputCommentDto(Long id, String text, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.created = created;
    }
}
