package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.InputBookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class InputBookingDtoTest {
    @Autowired
    private JacksonTester<InputBookingDto> json;

    @Test
    void testBooking() throws Exception {
        InputBookingDto booking = new InputBookingDto(
                LocalDateTime.of(2022, 10, 1, 12, 0, 0),
                LocalDateTime.of(2022, 10, 2, 12, 0, 0),
                1L
        );

        JsonContent<InputBookingDto> result = json.write(booking);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}