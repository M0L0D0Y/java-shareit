package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.InputUserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class InputUserDtoTest {
    @Autowired
    private JacksonTester<InputUserDto> json;

    @Test
    void testInputUserDto() throws Exception {
        InputUserDto user = new InputUserDto("John", "john.doe@mail.com");

        JsonContent<InputUserDto> result = json.write(user);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john.doe@mail.com");
    }

}