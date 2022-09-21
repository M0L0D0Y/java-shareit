package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.OutputUserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class OutputUserDtoTest {
    @Autowired
    private JacksonTester<OutputUserDto> json;

    @Test
    void testOutputUserDto() throws Exception {
        OutputUserDto user = new OutputUserDto(
                1L, "John", "john.doe@mail.com");

        JsonContent<OutputUserDto> result = json.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john.doe@mail.com");
    }

}