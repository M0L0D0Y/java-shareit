package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingController(BookingService bookingService, BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

    @PostMapping
    public OutputBookingDto addBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                       @Valid @RequestBody InputBookingDto inputBookingDto) {
        Booking booking = bookingMapper.toBooking(inputBookingDto);
        return bookingMapper.toOutputBookingDto(bookingService.addBooking(userId, booking), userId);
    }

    @PatchMapping(value = "/{bookingId}")
    public OutputBookingDto confirmBookingByOwner(@RequestHeader(HEADER_USER_ID) long userId,
                                                  @PathVariable Long bookingId,
                                                  @RequestParam Boolean approved) {
        return bookingMapper.toOutputBookingDto(bookingService.confirmBookingByOwner(userId, bookingId, approved), userId);

    }

    @GetMapping(value = "/{bookingId}")
    public OutputBookingDto getBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                       @PathVariable Long bookingId) {
        return bookingMapper.toOutputBookingDto(bookingService.getBooking(userId, bookingId), userId);
    }

    @GetMapping()
    public List<OutputBookingDto> getBookingsByBookerId(@RequestHeader(HEADER_USER_ID) long userId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        List<Booking> bookingPage = bookingService.getBookingsByBookerId(userId, state, from, size);
        if (!bookingPage.isEmpty()) {
            return bookingPage.stream()
                    .map(booking -> bookingMapper.toOutputBookingDto(booking, userId))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @GetMapping(value = "/owner")
    public List<OutputBookingDto> getBookingsByIdOwnerItem(@RequestHeader(HEADER_USER_ID) long userId,
                                                           @RequestParam(defaultValue = "ALL") String state,
                                                           @RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<Booking> bookingPage = bookingService.getBookingsByIdOwnerItem(userId, state, from, size);
        if (!bookingPage.isEmpty()) {
            return bookingPage.stream()
                    .map(booking -> bookingMapper.toOutputBookingDto(booking, userId))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
