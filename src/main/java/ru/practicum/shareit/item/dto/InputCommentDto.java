package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InputCommentDto {
    @NotBlank(message = "Пустой отзыв")
    private String text;

    public InputCommentDto() {
    }

    public InputCommentDto(String text) {
        this.text = text;
    }
}

