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
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                LocalDateTime.of(2022, 10, 1, 12, 0, 0),
                LocalDateTime.of(2022, 10, 2, 12, 0, 0),
                1L);
        booking = new Booking(1L,
                LocalDateTime.of(2022, 10, 1, 12, 0, 0),
                LocalDateTime.of(2022, 10, 2, 12, 0, 0),
                1L, 2L, Status.APPROVED);
        outputBookingDto = new OutputBookingDto(
                1L,
                LocalDateTime.of(2022, 10, 1, 12, 0, 0),
                LocalDateTime.of(2022, 10, 2, 12, 0, 0),
                new OutputItemDto(),
                new OutputUserDto(),
                Status.APPROVED);
    }

    @Test
    void addBookingNullHeader() throws Exception {
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(inputBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBooking() throws Exception {
        when(bookingMapper.toBooking(any(InputBookingDto.class)))
                .thenReturn(booking);
        when(bookingService.addBooking(anyLong(), any(Booking.class)))
                .thenReturn(booking);
        when(bookingMapper.toOutputBookingDto(any(Booking.class), anyLong()))
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
    void confirmBookingByOwnerNullHeader() throws Exception {
        mockMvc.perform(patch("/bookings/{bookingId}", "1")
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());

    }

    @Test
    void confirmBookingByOwner() throws Exception {
        when(bookingService.confirmBookingByOwner(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(booking);
        when(bookingMapper.toOutputBookingDto(any(Booking.class), anyLong()))
                .thenReturn(outputBookingDto);
        mockMvc.perform(patch("/bookings/{bookingId}", "1")
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
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
                .confirmBookingByOwner(1L, 1L, true);
    }


    @Test
    void getBookingNullHeader() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", "1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(booking);
        when(bookingMapper.toOutputBookingDto(any(Booking.class), anyLong()))
                .thenReturn(outputBookingDto);
        mockMvc.perform(get("/bookings/{bookingId}", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
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
                .getBooking(1L, 1L);
    }

    @Test
    void getBookingsByBookerIdStateNull() throws Exception {
        when(bookingService.getBookingsByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toOutputBookingDto(any(Booking.class), anyLong()))
                .thenReturn(outputBookingDto);
        mockMvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HEADER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.singletonList(outputBookingDto))));
        verify(bookingService, times(1))
                .getBookingsByBookerId(1L, "ALL", 0, 10);
    }

    @Test
    void getBookingsByBookerIdEmptyList() throws Exception {
        when(bookingService.getBookingsByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of());
        mockMvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HEADER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.emptyList())));
        verify(bookingService, times(1))
                .getBookingsByBookerId(1L, "ALL", 0, 10);
    }

    @Test
    void getBookingsByBookerIdStatePast() throws Exception {
        when(bookingService.getBookingsByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toOutputBookingDto(any(Booking.class), anyLong()))
                .thenReturn(outputBookingDto);
        mockMvc.perform(get("/bookings")
                        .param("state", "PAST")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HEADER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.singletonList(outputBookingDto))));
        verify(bookingService, times(1))
                .getBookingsByBookerId(1L, "PAST", 0, 10);
    }

    @Test
    void getBookingsByBookerIdNullList() throws Exception {
        when(bookingService.getBookingsByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of());
        mockMvc.perform(get("/bookings")
                        .param("state", "PAST")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HEADER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.emptyList())));
        verify(bookingService, times(1))
                .getBookingsByBookerId(1L, "PAST", 0, 10);
    }

    @Test
    void getBookingsByIdOwnerItemStatusNull() throws Exception {
        when(bookingService.getBookingsByIdOwnerItem(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toOutputBookingDto(any(Booking.class), anyLong()))
                .thenReturn(outputBookingDto);
        mockMvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HEADER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.singletonList(outputBookingDto))));
        verify(bookingService, times(1))
                .getBookingsByIdOwnerItem(1L, "ALL", 0, 10);
    }

    @Test
    void getBookingsByIdOwnerItemStatusPast() throws Exception {
        when(bookingService.getBookingsByIdOwnerItem(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toOutputBookingDto(any(Booking.class), anyLong()))
                .thenReturn(outputBookingDto);
        mockMvc.perform(get("/bookings/owner")
                        .param("state", "PAST")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HEADER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.singletonList(outputBookingDto))));
        verify(bookingService, times(1))
                .getBookingsByIdOwnerItem(1L, "PAST", 0, 10);
    }

    @Test
    void getBookingsByIdOwnerItemNullList() throws Exception {
        when(bookingService.getBookingsByIdOwnerItem(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(bookingMapper.toOutputBookingDto(any(Booking.class), anyLong()))
                .thenReturn(outputBookingDto);
        mockMvc.perform(get("/bookings/owner")
                        .param("state", "PAST")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HEADER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.emptyList())));
        verify(bookingService, times(1))
                .getBookingsByIdOwnerItem(1L, "PAST", 0, 10);
    }
}