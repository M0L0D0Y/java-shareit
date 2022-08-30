package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.OutputItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.OutputUserDto;
import ru.practicum.shareit.user.dto.UserMapper;

@Component
public class BookingMapper {
    private final ItemStorage itemStorage;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Autowired
    public BookingMapper(ItemStorage itemStorage,
                         UserService userService,
                         UserMapper userMapper,
                         ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.userMapper = userMapper;
        this.itemMapper = itemMapper;
    }

    public OutputBookingDto toOutputBookingDto(Booking booking, long userId) {
        OutputBookingDto outputBookingDto = new OutputBookingDto();
        outputBookingDto.setId(booking.getId());
        outputBookingDto.setStart(booking.getStart());
        outputBookingDto.setEnd(booking.getEnd());
        Item item = itemStorage.findById(booking.getItemId())
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет вещи с таким id " + booking.getItemId()));
        OutputItemDto outputItemDto = itemMapper.toOutputItemDto(item, userId);
        outputBookingDto.setItem(outputItemDto);
        User user = userService.getUser(booking.getBookerId());
        OutputUserDto outputUserDto = userMapper.toOutputUserDto(user);
        outputBookingDto.setBooker(outputUserDto);
        outputBookingDto.setStatus(booking.getStatus());
        return outputBookingDto;
    }

    public Booking toBooking(InputBookingDto inputBookingDto) {
        Booking booking = new Booking();
        booking.setStart(inputBookingDto.getStart());
        booking.setEnd(inputBookingDto.getEnd());
        booking.setItemId(inputBookingDto.getItemId());
        return booking;
    }
}
