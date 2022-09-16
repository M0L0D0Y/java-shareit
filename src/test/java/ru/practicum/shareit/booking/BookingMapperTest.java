package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.OutputItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.OutputUserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookingMapperTest {
    BookingMapper bookingMapper;
    ItemService itemService;
    UserService userService;
    UserMapper userMapper;
    ItemMapper itemMapper;
    InputBookingDto inputBookingDto;
    Booking booking;
    OutputBookingDto outputBookingDto;
    Item item;
    OutputItemDto outputItemDto;
    User user;
    OutputUserDto outputUserDto;

    @BeforeEach
    void beforeEach() {
        itemMapper = mock(ItemMapper.class);
        userMapper = mock(UserMapper.class);
        userService = mock(UserServiceImpl.class);
        itemService = mock(ItemServiceImpl.class);
        bookingMapper = new BookingMapper(itemService, userService, userMapper, itemMapper);
        item = new Item(1L, "name", "description", true, 1L, null);
        outputItemDto = new OutputItemDto(1L, "name", "description",
                true, new BookingDto(), new BookingDto(), null);
        user = new User(1L, "user", "user@mail.ru");
        outputUserDto = new OutputUserDto(1L, "user", "user@mail.ru");
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
                outputItemDto,
                outputUserDto,
                Status.APPROVED);

    }

    @Test
    void toOutputBookingDto() {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(item);
        when(itemMapper.toOutputItemDto(any(Item.class), anyLong()))
                .thenReturn(outputItemDto);
        when(userService.getUser(anyLong()))
                .thenReturn(user);
        when(userMapper.toOutputUserDto(any(User.class)))
                .thenReturn(outputUserDto);
        final OutputBookingDto outputBookingDto1 = bookingMapper.toOutputBookingDto(booking, 1L);
        assertEquals(outputBookingDto.getId(), outputBookingDto1.getId());
        assertEquals(outputBookingDto.getStart(), outputBookingDto1.getStart());
        assertEquals(outputBookingDto.getEnd(), outputBookingDto1.getEnd());
        assertEquals(outputBookingDto.getItem(), outputBookingDto1.getItem());
        assertEquals(outputBookingDto.getBooker(), outputBookingDto1.getBooker());
        assertEquals(outputBookingDto.getStatus(), outputBookingDto1.getStatus());
    }

    @Test
    void toBooking() {
        Booking booking1 = bookingMapper.toBooking(inputBookingDto);
        assertEquals(booking.getStart(), booking1.getStart());
        assertEquals(booking.getEnd(), booking1.getEnd());
        assertEquals(booking.getItemId(), booking1.getItemId());
    }
}