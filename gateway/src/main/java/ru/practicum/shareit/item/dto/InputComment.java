package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InputComment {
    @NotBlank(message = "Пустой отзыв")
    private String text;

    public InputComment() {
    }

    public InputComment(String text) {
        this.text = text;
    }
}
