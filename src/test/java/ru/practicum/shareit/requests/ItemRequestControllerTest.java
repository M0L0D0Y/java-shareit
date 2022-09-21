package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.requests.dto.InputItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.OutputItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    long userId = 1L;
    @MockBean
    ItemRequestMapper itemRequestMapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    InputItemRequestDto inputItemRequestDto;
    ItemRequest itemRequest;
    OutputItemRequestDto outputItemRequestDto;

    @BeforeEach
    void beforeEach() {
        inputItemRequestDto = new InputItemRequestDto("description");
        itemRequest = new ItemRequest(1L, "description", 1L, LocalDateTime.now());
        outputItemRequestDto =
                new OutputItemRequestDto(1L, "description", LocalDateTime.now(), new ArrayList<>());
    }

    @Test
    void addItemRequestNullHeader() throws Exception {
        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(inputItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addItemRequest() throws Exception {
        when(itemRequestMapper.toItemRequest(inputItemRequestDto))
                .thenReturn(itemRequest);
        when(itemRequestService.addItemRequest(userId, itemRequest))
                .thenReturn(itemRequest);
        when(itemRequestMapper.toOutputItemRequestDto(userId, itemRequest))
                .thenReturn(outputItemRequestDto);

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(inputItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputItemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(outputItemRequestDto.getDescription()))
                .andExpect(jsonPath("$.items").value(outputItemRequestDto.getItems()));

        verify(itemRequestService, times(1))
                .addItemRequest(userId, itemRequest);
    }

    @Test
    void getAllMeItemRequestNullHeader() throws Exception {
        mockMvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllMeItemRequest() throws Exception {
        when(itemRequestService.getAllMeRequest(userId))
                .thenReturn((List.of(itemRequest)));
        when(itemRequestMapper.toOutputItemRequestDto(userId, itemRequest))
                .thenReturn(outputItemRequestDto);

        mockMvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.singletonList(outputItemRequestDto))));
        verify(itemRequestService, times(1))
                .getAllMeRequest(userId);
    }

    @Test
    void getAllItemRequestNullHeader() throws Exception {
        mockMvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllItemRequest() throws Exception {
        when(itemRequestService.getAllItemRequest(userId, 0,10))
                .thenReturn((List.of(itemRequest)));
        when(itemRequestMapper.toOutputItemRequestDto(userId, itemRequest))
                .thenReturn(outputItemRequestDto);

        mockMvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.singletonList(outputItemRequestDto))));
        verify(itemRequestService, times(1))
                .getAllItemRequest(userId, 0,10);
    }

    @Test
    void getItemRequestByIdNullHeader() throws Exception {
        mockMvc.perform(get("/requests/{requestId}", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemRequestById() throws Exception {
        when(itemRequestService.getItemRequestById(userId, itemRequest.getId()))
                .thenReturn(itemRequest);
        when(itemRequestMapper.toOutputItemRequestDto(userId, itemRequest))
                .thenReturn(outputItemRequestDto);

        mockMvc.perform(get("/requests/{requestId}", "1")
                        .content(mapper.writeValueAsString(inputItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputItemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(outputItemRequestDto.getDescription()))
                .andExpect(jsonPath("$.items").value(outputItemRequestDto.getItems()));

        verify(itemRequestService, times(1))
                .getItemRequestById(userId, itemRequest.getId());
    }
}