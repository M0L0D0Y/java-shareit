package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.requests.dto.OutputItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class OutputItemRequestDtoTest {
    @Autowired
    private JacksonTester<OutputItemRequestDto> json;

    @Test
    void testInputItemRequestDto() throws Exception {
        OutputItemRequestDto outputItemRequestDto = new OutputItemRequestDto(
                1L,
                "description",
                LocalDateTime.now(),
                new ArrayList<>());

        JsonContent<OutputItemRequestDto> result = json.write(outputItemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(outputItemRequestDto.getItems());
    }
}