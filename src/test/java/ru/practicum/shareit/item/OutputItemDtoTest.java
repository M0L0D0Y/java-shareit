package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.OutputItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class OutputItemDtoTest {
    @Autowired
    private JacksonTester<OutputItemDto> json;

    @Test
    void testComment() throws Exception {
        OutputItemDto item = new OutputItemDto(
                1L,
                "name",
                "description",
                true,
                new BookingDto(),
                new BookingDto(),
                2L
        );

        JsonContent<OutputItemDto> result = json.write(item);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }
}