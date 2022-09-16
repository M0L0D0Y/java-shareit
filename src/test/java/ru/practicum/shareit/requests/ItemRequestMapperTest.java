package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.OutputItemDto;
import ru.practicum.shareit.requests.dto.InputItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.OutputItemRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemRequestMapperTest {
    ItemMapper itemMapper;
    ItemService itemService;
    ItemRequestMapper itemRequestMapper;
    ItemRequest itemRequest;
    InputItemRequestDto inputItemRequestDto;
    OutputItemRequestDto outputItemRequestDto1;
    OutputItemRequestDto outputItemRequestDto2;
    Item item;
    OutputItemDto outputItemDto;

    @BeforeEach
    void beforeEach() {
        itemMapper = mock(ItemMapper.class);
        itemService = mock(ItemServiceImpl.class);
        itemRequestMapper = new ItemRequestMapper(itemService, itemMapper);
        itemRequest = new ItemRequest(1L, "description", 1L, LocalDateTime.now());
        inputItemRequestDto = new InputItemRequestDto("description");
        outputItemDto = new OutputItemDto(1L, "name", "description",
                true, new BookingDto(), new BookingDto(), null);
        outputItemRequestDto1 = new OutputItemRequestDto(1L,
                "description",
                LocalDateTime.now(),
                new ArrayList<>());
        outputItemRequestDto2 = new OutputItemRequestDto(1L,
                "description",
                LocalDateTime.now(),
                List.of(outputItemDto));
        item = new Item(1L, "item", "description", true, 1L, null);
    }

    @Test
    void toOutputItemRequestDtoEmptyListItem() {
        when(itemService.getAllItemByRequestId(anyLong(), anyLong()))
                .thenReturn(Collections.emptyList());
        final OutputItemRequestDto dto = itemRequestMapper.toOutputItemRequestDto(1L, itemRequest);
        assertEquals(outputItemRequestDto1.getId(), dto.getId());
        assertEquals(outputItemRequestDto1.getDescription(), dto.getDescription());
        assertEquals(outputItemRequestDto1.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                dto.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertEquals(outputItemRequestDto1.getItems().size(), dto.getItems().size());
    }

    @Test
    void toOutputItemRequestDtoListItem() {
        when(itemService.getAllItemByRequestId(anyLong(), anyLong()))
                .thenReturn(Collections.singletonList(item));
        when(itemMapper.toOutputItemDto(any(Item.class), anyLong()))
                .thenReturn(outputItemDto);
        final OutputItemRequestDto dto = itemRequestMapper.toOutputItemRequestDto(1L, itemRequest);
        assertEquals(outputItemRequestDto2.getId(), dto.getId());
        assertEquals(outputItemRequestDto2.getDescription(), dto.getDescription());
        assertEquals(outputItemRequestDto2.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                dto.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertEquals(outputItemRequestDto2.getItems().size(), dto.getItems().size());
    }

    @Test
    void toItemRequest() {
        final ItemRequest itemRequest1 = itemRequestMapper.toItemRequest(inputItemRequestDto);
        assertEquals(itemRequest.getDescription(), itemRequest1.getDescription());
    }
}