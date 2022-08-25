package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

@Component
public class BookingMapper {
    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public BookingMapper(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    public OutputBookingDto toOutputBookingDto(Booking booking) {
        OutputBookingDto outputBookingDto = new OutputBookingDto();
        outputBookingDto.setId(booking.getId());
        outputBookingDto.setStart(booking.getStart());
        outputBookingDto.setEnd(booking.getEnd());
        Item item = itemService.getItem(booking.getBookerId(), booking.getItemId());
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
        booking.setItemId(inputBookingDto.itemId);
        return booking;
    }
}
