package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.InputItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class InputItemDtoTest {
    @Autowired
    private JacksonTester<InputItemDto> json;

    @Test
    void testComment() throws Exception {
        InputItemDto item = new InputItemDto("name", "description", true, 2L);

        JsonContent<InputItemDto> result = json.write(item);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }
}