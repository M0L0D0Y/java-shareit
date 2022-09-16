package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.OutputItemDtoWithComment;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class OutputItemDtoWithCommentTest {
    @Autowired
    private JacksonTester<OutputItemDtoWithComment> json;

    @Test
    void testComment() throws Exception {
        OutputItemDtoWithComment item = new OutputItemDtoWithComment(
                1L,
                "name",
                "description",
                true,
                new BookingDto(),
                new BookingDto(),
                new ArrayList<>()
        );

        JsonContent<OutputItemDtoWithComment> result = json.write(item);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }
}