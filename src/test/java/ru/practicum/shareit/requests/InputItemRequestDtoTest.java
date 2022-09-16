package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.requests.dto.InputItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class InputItemRequestDtoTest {
    @Autowired
    private JacksonTester<InputItemRequestDto> json;

    @Test
    void testInputItemRequestDto() throws Exception {
        InputItemRequestDto inputItemRequestDto = new InputItemRequestDto("description");

        JsonContent<InputItemRequestDto> result = json.write(inputItemRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }

}