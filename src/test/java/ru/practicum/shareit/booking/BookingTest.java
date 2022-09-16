package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingTest {
    @Autowired
    private JacksonTester<Booking> json;

    @Test
    void testBooking() throws Exception {
        Booking booking = new Booking(
                1L,
                LocalDateTime.of(2022, 10, 1, 12, 0, 0),
                LocalDateTime.of(2022, 10, 2, 12, 0, 0),
                1L,
                2L,
                Status.APPROVED);

        JsonContent<Booking> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(Status.APPROVED.toString());
    }
}