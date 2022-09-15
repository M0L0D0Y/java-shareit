package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.item.dto.OutputItemDto;
import ru.practicum.shareit.user.dto.OutputUserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    @MockBean
    BookingService bookingService;
    @MockBean
    BookingMapper bookingMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    InputBookingDto inputBookingDto;
    Booking booking;
    OutputBookingDto outputBookingDto;

    @BeforeEach
    void beforeEach() {
        inputBookingDto = new InputBookingDto(
                LocalDateTime.of(2022, 10, 1, 12, 0,0),
                LocalDateTime.of(2022, 10, 2, 12, 0,0),
                1L);
        booking = new Booking(1L,
                LocalDateTime.of(2022, 10, 1, 12, 0, 0),
                LocalDateTime.of(2022, 10, 2, 12, 0, 0),
                1L, 2L, Status.APPROVED);
        outputBookingDto = new OutputBookingDto(
                1L,
                LocalDateTime.of(2022, 10, 1, 12, 0,0),
                LocalDateTime.of(2022, 10, 2, 12, 0,0),
                new OutputItemDto(),
                new OutputUserDto(),
                Status.APPROVED);
    }

    @Test
    void addBooking() throws Exception {
        when(bookingMapper.toBooking(inputBookingDto))
                .thenReturn(booking);
        when(bookingService.addBooking(1L, booking))
                .thenReturn(booking);
        when(bookingMapper.toOutputBookingDto(booking, 1L))
                .thenReturn(outputBookingDto);

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(inputBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputBookingDto.getId()))
                .andExpect(jsonPath("$.start").value(outputBookingDto
                        .getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
                .andExpect(jsonPath("$.end").value(outputBookingDto
                        .getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
                .andExpect(jsonPath("$.item").value(outputBookingDto.getItem()))
                .andExpect(jsonPath("$.booker").value(outputBookingDto.getBooker()))
                .andExpect(jsonPath("$.status").value(outputBookingDto.getStatus().toString()));

        verify(bookingService, times(1))
                .addBooking(1L, booking);
    }

    @Test
    void confirmBookingByOwner() {
    }

    @Test
    void getBooking() {
    }

    @Test
    void getBookingsByBookerId() {
    }

    @Test
    void getBookingsByIdOwnerItem() {
    }
}