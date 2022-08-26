package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

@Component
public class BookingMapper {
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Autowired
    public BookingMapper(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    public OutputBookingDto toOutputBookingDto(Booking booking) {
        OutputBookingDto outputBookingDto = new OutputBookingDto();
        outputBookingDto.setId(booking.getId());
        outputBookingDto.setStart(booking.getStart());
        outputBookingDto.setEnd(booking.getEnd());
        Item item = itemStorage.findById(booking.getItemId())
                .stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Нет вещи с таким id " + booking.getItemId()));
        outputBookingDto.setItem(item);
        User user = userService.getUser(booking.getBookerId());
        outputBookingDto.setBooker(user);
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
