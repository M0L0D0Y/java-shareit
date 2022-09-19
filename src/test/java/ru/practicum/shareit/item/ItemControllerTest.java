package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    @MockBean
    ItemService itemService;
    @MockBean
    ItemMapper itemMapper;
    @MockBean
    CommentMapper commentMapper;
    @MockBean
    BookingStorage bookingStorage;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    InputItemDto inputItemDto;
    Item item;
    OutputItemDto outputItemDto;
    OutputItemDtoWithComment outputItemDtoWithComment;
    InputCommentDto inputCommentDto;
    Comment comment;
    OutputCommentDto outputCommentDto;
    Booking booking;
    long user1Id = 1L;
    int from = 0;
    int size = 10;

    @BeforeEach
    void beforeEach() {
        inputItemDto = new InputItemDto("name", "description", true, null);
        item = new Item(1L, "name", "description", true, new User(), null);
        outputItemDto = new OutputItemDto(1L, "name", "description",
                true, new BookingDto(), new BookingDto(), null);
        outputItemDtoWithComment = new OutputItemDtoWithComment(1L, "name", "description",
                true, new BookingDto(), new BookingDto(), new ArrayList<>());
        inputCommentDto = new InputCommentDto("comment");
        comment = new Comment(1L, "comment", 1L, 1L, LocalDateTime.now());
        outputCommentDto = new OutputCommentDto(1L, "comment", "name", LocalDateTime.now());
        booking = new Booking(1L,
                LocalDateTime.of(2022, 8, 1, 12, 0),
                LocalDateTime.of(2022, 8, 2, 12, 0),
                item.getId(), 2L, Status.APPROVED);
    }

    @Test
    void addItemNullHeader() throws Exception {
        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(inputItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addItem() throws Exception {
        when(itemMapper.toItem(anyLong(),any(InputItemDto.class)))
                .thenReturn(item);
        when(itemService.addItem(anyLong(), any(Item.class)))
                .thenReturn(item);
        when(itemMapper.toOutputItemDto(any(Item.class), anyLong()))
                .thenReturn(outputItemDto);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(inputItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, user1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputItemDto.getId()))
                .andExpect(jsonPath("$.name").value(outputItemDto.getName()))
                .andExpect(jsonPath("$.description").value(outputItemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(outputItemDto.getAvailable()))
                .andExpect(jsonPath("$.lastBooking").value(outputItemDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(outputItemDto.getNextBooking()))
                .andExpect(jsonPath("$.requestId").value(outputItemDto.getRequestId()));

        verify(itemService, times(1))
                .addItem(user1Id, item);
    }

    @Test
    void updateItemNullHeader() throws Exception {
        mockMvc.perform(patch("/items/{id}", "1")
                        .content(mapper.writeValueAsString(inputItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem() throws Exception {
        when(itemMapper.toItem(anyLong(), any(InputItemDto.class)))
                .thenReturn(item);
        when(itemService.updateItem(anyLong(), anyLong(), any(Item.class)))
                .thenReturn(item);
        when(itemMapper.toOutputItemDto(any(Item.class), anyLong()))
                .thenReturn(outputItemDto);

        mockMvc.perform(patch("/items/{id}", "1")
                        .content(mapper.writeValueAsString(inputItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, user1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputItemDto.getId()))
                .andExpect(jsonPath("$.name").value(outputItemDto.getName()))
                .andExpect(jsonPath("$.description").value(outputItemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(outputItemDto.getAvailable()))
                .andExpect(jsonPath("$.lastBooking").value(outputItemDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(outputItemDto.getNextBooking()))
                .andExpect(jsonPath("$.requestId").value(outputItemDto.getRequestId()));

        verify(itemService, times(1))
                .updateItem(user1Id, item.getId(), item);
    }

    @Test
    void getItemNullHeader() throws Exception {
        mockMvc.perform(get("/items/{id}", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItem() throws Exception {
        when(itemMapper.toItem(anyLong(), any(InputItemDto.class)))
                .thenReturn(item);
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(item);
        when(itemMapper.toOutputItemDto(any(Item.class), anyLong()))
                .thenReturn(outputItemDto);
        when(itemMapper.toOutputItemDtoWithComment(any(OutputItemDto.class)))
                .thenReturn(outputItemDtoWithComment);

        mockMvc.perform(get("/items/{id}", "1")
                        .content(mapper.writeValueAsString(inputItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, user1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputItemDtoWithComment.getId()))
                .andExpect(jsonPath("$.name").value(outputItemDtoWithComment.getName()))
                .andExpect(jsonPath("$.description").value(outputItemDtoWithComment.getDescription()))
                .andExpect(jsonPath("$.available").value(outputItemDtoWithComment.getAvailable()))
                .andExpect(jsonPath("$.lastBooking").value(outputItemDtoWithComment.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(outputItemDtoWithComment.getNextBooking()))
                .andExpect(jsonPath("$.comments").value(outputItemDtoWithComment.getComments()));

        verify(itemService, times(1))
                .getItem(user1Id, item.getId());
    }

    @Test
    void getAllItemNullHeader() throws Exception {
        mockMvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllItem() throws Exception {
        when(itemService.getAllItem(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(item));
        when(itemMapper.toOutputItemDto(any(Item.class), anyLong()))
                .thenReturn(outputItemDto);
        when(itemMapper.toOutputItemDtoWithComment(any(OutputItemDto.class)))
                .thenReturn(outputItemDtoWithComment);

        mockMvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, user1Id))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.singletonList(outputItemDtoWithComment))));

        verify(itemService, times(1))
                .getAllItem(user1Id, from, size);
    }

    @Test
    void searchItemByTextNullHeader() throws Exception {
        mockMvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchItemByNullText() throws Exception {
        String text = " ";
        when(itemService.searchItemByText(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of());
        when(itemMapper.toOutputItemDto(any(Item.class), anyLong()))
                .thenReturn(outputItemDto);
        mockMvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, user1Id)
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.emptyList())));
        verify(itemService, times(1))
                .searchItemByText(user1Id, text, from, size);
    }

    @Test
    void searchItemByText() throws Exception {
        String text = "text";
        when(itemService.searchItemByText(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(item));
        when(itemMapper.toOutputItemDto(any(Item.class), anyLong()))
                .thenReturn(outputItemDto);
        mockMvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, user1Id)
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(
                        Collections.singletonList(outputItemDto))));
        verify(itemService, times(1))
                .searchItemByText(user1Id, text, from, size);
    }

    @Test
    void addComment() throws Exception {
        when(commentMapper.toComment(any(InputCommentDto.class)))
                .thenReturn(comment);
        when(itemService.addComment(anyLong(), anyLong(), any(Comment.class)))
                .thenReturn(comment);
        when(commentMapper.toOutputCommentDto(any(Comment.class)))
                .thenReturn(outputCommentDto);

        mockMvc.perform(post("/items/{itemId}/comment", "1")
                        .content(mapper.writeValueAsString(inputCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputCommentDto.getId()))
                .andExpect(jsonPath("$.text").value(outputCommentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(outputCommentDto.getAuthorName()));

        verify(itemService, times(1))
                .addComment(2L, item.getId(), comment);

    }
}