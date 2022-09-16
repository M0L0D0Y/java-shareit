package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemMapperTest {
    ItemMapper itemMapper;
    BookingService bookingService;
    ItemService itemService;
    CommentMapper commentMapper;
    InputItemDto inputItemDto;
    Item item;
    OutputItemDto outputItemDto;
    OutputItemDto outputItemDto1;
    OutputItemDto outputItemDto2;
    OutputItemDto outputItemDto3;
    OutputItemDtoWithComment outputItemDtoWithComment1;
    OutputItemDtoWithComment outputItemDtoWithComment2;
    OutputItemDtoWithComment outputItemDtoWithComment3;
    OutputItemDtoWithComment outputItemDtoWithComment4;
    Booking booking1;
    Booking booking2;
    BookingDto bookingDto1;
    BookingDto bookingDto2;
    Comment comment1;
    Comment comment2;
    OutputCommentDto outputCommentDto1;
    OutputCommentDto outputCommentDto2;

    @BeforeEach
    void beforeEach() {
        bookingService = mock(BookingServiceImpl.class);
        itemService = mock(ItemServiceImpl.class);
        commentMapper = mock(CommentMapper.class);
        itemMapper = new ItemMapper(bookingService, itemService, commentMapper);
        comment1 = new Comment(1L, "comment", 1L, 1L, LocalDateTime.now());
        comment2 = new Comment(2L, "comment", 1L, 1L, LocalDateTime.now());
        outputCommentDto1 = new OutputCommentDto(1L, "comment", "name", LocalDateTime.now());
        outputCommentDto2 = new OutputCommentDto(2L, "comment", "name", LocalDateTime.now());
        booking1 = new Booking(1L,
                LocalDateTime.of(2022, 1, 1, 12, 0, 0),
                LocalDateTime.of(2022, 1, 2, 12, 0, 0),
                1L, 2L, Status.APPROVED);
        booking2 = new Booking(2L,
                LocalDateTime.of(2022, 1, 10, 12, 0, 0),
                LocalDateTime.of(2022, 1, 11, 12, 0, 0),
                1L, 2L, Status.APPROVED);
        bookingDto1 = new BookingDto(1L, 2L);
        bookingDto2 = new BookingDto(2L, 2L);
        inputItemDto = new InputItemDto("name", "description", true, null);
        item = new Item(1L, "name", "description", true, 1L, null);
        outputItemDto = new OutputItemDto(1L, "name", "description",
                true, bookingDto1, bookingDto2, null);
        outputItemDto1 = new OutputItemDto(1L, "name", "description",
                true, null, bookingDto2, null);
        outputItemDto2 = new OutputItemDto(1L, "name", "description",
                true, bookingDto1, null, null);
        outputItemDto3 = new OutputItemDto(1L, "name", "description",
                true, null, null, null);
        outputItemDtoWithComment1 = new OutputItemDtoWithComment(1L, "name", "description",
                true, bookingDto1, bookingDto2, List.of(outputCommentDto1, outputCommentDto2));
        outputItemDtoWithComment2 = new OutputItemDtoWithComment(1L, "name", "description",
                true, bookingDto1, bookingDto2, List.of());

        outputItemDtoWithComment3 = new OutputItemDtoWithComment(1L, "name", "description",
                true, bookingDto1, null, List.of(outputCommentDto1));
        outputItemDtoWithComment4 = new OutputItemDtoWithComment(1L, "name", "description",
                true, null, bookingDto2, List.of());
    }

    @Test
    void toOutputItemDtoOwner() {
        when(bookingService.getBookingsByItemId(anyLong()))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));
        when(bookingService.findAllPastBookingsByItemId(anyLong(), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));
        when(bookingService.getBookingsByItemId(anyLong()))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));
        when(bookingService.findAllFutureBookingsByItemId(anyLong(), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));

        final OutputItemDto outputItemDto1 = itemMapper.toOutputItemDto(item, 1L);
        assertEquals(outputItemDto.getId(), outputItemDto1.getId());
        assertEquals(outputItemDto.getName(), outputItemDto1.getName());
        assertEquals(outputItemDto.getDescription(), outputItemDto1.getDescription());
        assertEquals(outputItemDto.getLastBooking(), outputItemDto1.getLastBooking());
        assertEquals(outputItemDto.getNextBooking(), outputItemDto1.getNextBooking());
        assertEquals(outputItemDto.getAvailable(), outputItemDto1.getAvailable());
    }

    @Test
    void toOutputItemDtoOwnerNullLastBooking() {
        when(bookingService.getBookingsByItemId(anyLong()))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));
        when(bookingService.findAllPastBookingsByItemId(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(bookingService.getBookingsByItemId(anyLong()))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));
        when(bookingService.findAllFutureBookingsByItemId(anyLong(), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));

        final OutputItemDto outputItemDto1 = itemMapper.toOutputItemDto(item, 1L);
        assertEquals(outputItemDto1.getId(), outputItemDto1.getId());
        assertEquals(outputItemDto1.getName(), outputItemDto1.getName());
        assertEquals(outputItemDto1.getDescription(), outputItemDto1.getDescription());
        assertEquals(outputItemDto1.getLastBooking(), outputItemDto1.getLastBooking());
        assertEquals(outputItemDto1.getNextBooking(), outputItemDto1.getNextBooking());
        assertEquals(outputItemDto1.getAvailable(), outputItemDto1.getAvailable());
    }

    @Test
    void toOutputItemDtoOwnerNullNextBooking() {
        when(bookingService.getBookingsByItemId(anyLong()))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));
        when(bookingService.findAllPastBookingsByItemId(anyLong(), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));
        when(bookingService.getBookingsByItemId(anyLong()))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));
        when(bookingService.findAllFutureBookingsByItemId(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of());

        final OutputItemDto outputItemDto1 = itemMapper.toOutputItemDto(item, 1L);
        assertEquals(outputItemDto2.getId(), outputItemDto1.getId());
        assertEquals(outputItemDto2.getName(), outputItemDto1.getName());
        assertEquals(outputItemDto2.getDescription(), outputItemDto1.getDescription());
        assertEquals(outputItemDto2.getLastBooking(), outputItemDto1.getLastBooking());
        assertEquals(outputItemDto2.getNextBooking(), outputItemDto1.getNextBooking());
        assertEquals(outputItemDto2.getAvailable(), outputItemDto1.getAvailable());
    }

    @Test
    void toOutputItemDtoOwnerNullBooking() {
        when(bookingService.getBookingsByItemId(anyLong()))
                .thenReturn(List.of());
        when(bookingService.getBookingsByItemId(anyLong()))
                .thenReturn(List.of());

        final OutputItemDto outputItemDto1 = itemMapper.toOutputItemDto(item, 1L);
        assertEquals(outputItemDto3.getId(), outputItemDto1.getId());
        assertEquals(outputItemDto3.getName(), outputItemDto1.getName());
        assertEquals(outputItemDto3.getDescription(), outputItemDto1.getDescription());
        assertEquals(outputItemDto3.getLastBooking(), outputItemDto1.getLastBooking());
        assertEquals(outputItemDto3.getNextBooking(), outputItemDto1.getNextBooking());
        assertEquals(outputItemDto3.getAvailable(), outputItemDto1.getAvailable());
    }

    @Test
    void toOutputItemDto() {
        when(bookingService.getBookingsByItemId(anyLong()))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));
        when(bookingService.findAllPastBookingsByItemId(anyLong(), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>(List.of(booking1, booking2)));
        final OutputItemDto outputItemDto1 = itemMapper.toOutputItemDto(item, 2L);
        assertEquals(outputItemDto.getId(), outputItemDto1.getId());
        assertEquals(outputItemDto.getName(), outputItemDto1.getName());
        assertEquals(outputItemDto.getDescription(), outputItemDto1.getDescription());
        assertEquals(outputItemDto.getAvailable(), outputItemDto1.getAvailable());

    }

    @Test
    void toItem() {
        Item item1 = itemMapper.toItem(inputItemDto);
        assertEquals(item.getName(), item1.getName());
        assertEquals(item.getDescription(), item1.getDescription());
        assertEquals(item.getAvailable(), item1.getAvailable());
    }

    @Test
    void toOutputItemDtoWithComment() {
        when(itemService.getCommentsByItemID(anyLong()))
                .thenReturn(new ArrayList<>(List.of(comment1, comment2)));
        when(commentMapper.toOutputCommentDto(any(Comment.class)))
                .thenReturn(outputCommentDto1);
        when(commentMapper.toOutputCommentDto(any(Comment.class)))
                .thenReturn(outputCommentDto2);
        final OutputItemDtoWithComment outputItemDto1 = itemMapper.toOutputItemDtoWithComment(outputItemDto);
        assertEquals(outputItemDtoWithComment1.getId(), outputItemDto1.getId());
        assertEquals(outputItemDtoWithComment1.getName(), outputItemDto1.getName());
        assertEquals(outputItemDtoWithComment1.getDescription(), outputItemDto1.getDescription());
        assertEquals(outputItemDtoWithComment1.getLastBooking(), outputItemDto1.getLastBooking());
        assertEquals(outputItemDtoWithComment1.getNextBooking(), outputItemDto1.getNextBooking());
        assertEquals(outputItemDtoWithComment1.getAvailable(), outputItemDto1.getAvailable());
        assertEquals(outputItemDtoWithComment1.getComments().size(), outputItemDto1.getComments().size());
    }

    @Test
    void toOutputItemDtoWithCommentNullLastBooking() {
        when(itemService.getCommentsByItemID(anyLong()))
                .thenReturn(List.of());
        when(commentMapper.toOutputCommentDto(any(Comment.class)))
                .thenReturn(outputCommentDto1);
        when(commentMapper.toOutputCommentDto(any(Comment.class)))
                .thenReturn(outputCommentDto2);
        final OutputItemDtoWithComment outputItem = itemMapper.toOutputItemDtoWithComment(outputItemDto1);
        assertEquals(outputItemDtoWithComment4.getId(), outputItem.getId());
        assertEquals(outputItemDtoWithComment4.getName(), outputItem.getName());
        assertEquals(outputItemDtoWithComment4.getDescription(), outputItem.getDescription());
        assertEquals(outputItemDtoWithComment4.getLastBooking(), outputItem.getLastBooking());
        assertEquals(outputItemDtoWithComment4.getNextBooking(), outputItem.getNextBooking());
        assertEquals(outputItemDtoWithComment4.getAvailable(), outputItem.getAvailable());
        assertEquals(outputItemDtoWithComment4.getComments().size(), outputItem.getComments().size());
    }

    @Test
    void toOutputItemDtoWithCommentNullNextBooking() {
        when(itemService.getCommentsByItemID(anyLong()))
                .thenReturn(new ArrayList<>(List.of(comment1)));
        when(commentMapper.toOutputCommentDto(any(Comment.class)))
                .thenReturn(outputCommentDto1);
        when(commentMapper.toOutputCommentDto(any(Comment.class)))
                .thenReturn(outputCommentDto2);
        final OutputItemDtoWithComment outputItem = itemMapper.toOutputItemDtoWithComment(outputItemDto2);
        assertEquals(outputItemDtoWithComment3.getId(), outputItem.getId());
        assertEquals(outputItemDtoWithComment3.getName(), outputItem.getName());
        assertEquals(outputItemDtoWithComment3.getDescription(), outputItem.getDescription());
        assertEquals(outputItemDtoWithComment3.getLastBooking(), outputItem.getLastBooking());
        assertEquals(outputItemDtoWithComment3.getNextBooking(), outputItem.getNextBooking());
        assertEquals(outputItemDtoWithComment3.getAvailable(), outputItem.getAvailable());
        assertEquals(outputItemDtoWithComment3.getComments().size(), outputItem.getComments().size());
    }

    @Test
    void toOutputItemDtoWithEmptyComment() {
        when(itemService.getCommentsByItemID(anyLong()))
                .thenReturn(List.of());
        final OutputItemDtoWithComment outputItemDto1 = itemMapper.toOutputItemDtoWithComment(outputItemDto);
        assertEquals(outputItemDtoWithComment2.getId(), outputItemDto1.getId());
        assertEquals(outputItemDtoWithComment2.getName(), outputItemDto1.getName());
        assertEquals(outputItemDtoWithComment2.getDescription(), outputItemDto1.getDescription());
        assertEquals(outputItemDtoWithComment2.getLastBooking(), outputItemDto1.getLastBooking());
        assertEquals(outputItemDtoWithComment2.getNextBooking(), outputItemDto1.getNextBooking());
        assertEquals(outputItemDtoWithComment2.getAvailable(), outputItemDto1.getAvailable());
        assertEquals(outputItemDtoWithComment2.getComments(), outputItemDto1.getComments());
    }
}