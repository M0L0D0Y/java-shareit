package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InputBookingDto;
import ru.practicum.shareit.booking.dto.OutputBookingDto;

import javax.validation.Valid;
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
        return bookingMapper.toOutputBookingDto(bookingService.addBooking(userId, booking));
    }

    @PatchMapping(value = "/{bookingId}")
    public OutputBookingDto approveOwner(@RequestHeader(HEADER_USER_ID) long userId,
                                         @PathVariable Long bookingId,
                                         @RequestParam Boolean approved) {
        return bookingMapper.toOutputBookingDto(bookingService.approveOwner(userId, bookingId, approved));

    }

    @GetMapping(value = "/{bookingId}")
    public OutputBookingDto getBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                       @PathVariable Long bookingId) {
        return bookingMapper.toOutputBookingDto(bookingService.getBooking(userId, bookingId));
    }

    @GetMapping()
    public List<OutputBookingDto> getBookingsByBooker(@RequestHeader(HEADER_USER_ID) long userId,
                                                      @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByBooker(userId, state)
                .stream()
                .map(bookingMapper::toOutputBookingDto)
                .collect(Collectors.toList());

    }

    @GetMapping(value = "/owner")
    public List<OutputBookingDto> getBookingsByOwner(@RequestHeader(HEADER_USER_ID) long userId,
                                                     @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByOwner(userId, state)
                .stream()
                .map(bookingMapper::toOutputBookingDto)
                .collect(Collectors.toList());
    }
}
