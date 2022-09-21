package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.InputCommentDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class InputCommentDtoTest {
    @Autowired
    private JacksonTester<InputCommentDto> json;

    @Test
    void testComment() throws Exception {
        InputCommentDto comment = new InputCommentDto("comment");

        JsonContent<InputCommentDto> result = json.write(comment);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("comment");
    }
}