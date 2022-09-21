package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.item.dto.OutputItemDto;
import ru.practicum.shareit.user.dto.OutputUserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class OutputBookingDtoTest {
    @Autowired
    private JacksonTester<OutputBookingDto> json;

    @Test
    void testBooking() throws Exception {
        OutputBookingDto booking = new OutputBookingDto(
                1L,
                LocalDateTime.of(2022, 10, 1, 12, 0, 0),
                LocalDateTime.of(2022, 10, 2, 12, 0, 0),
                new OutputItemDto(),
                new OutputUserDto(),
                Status.APPROVED);

        JsonContent<OutputBookingDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(Status.APPROVED.toString());
    }
}