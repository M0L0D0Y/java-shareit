package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.BookingValidateException;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                             @RequestBody @Valid BookItemRequestDto requestDto) {
        if (requestDto.getStart() == requestDto.getEnd()) {
            throw new BookingValidateException("Время старта равно времени окончания брони");
        }
        if (requestDto.getStart().isAfter(requestDto.getEnd())) {
            throw new BookingValidateException("Время старта позже времени окончания брони");
        }
        return bookingClient.addBooking(userId, requestDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> confirmBookingByOwner(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                                        @PathVariable @Positive Long bookingId,
                                                        @RequestParam(name = "approved") Boolean approved) {
        return bookingClient.confirmBookingByOwner(userId, bookingId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(HEADER_USER_ID) @Positive long userId,
                                             @PathVariable @Positive Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> getBookingsByBookerId(@RequestHeader(HEADER_USER_ID) long userId,
                                                        @RequestParam(name = "state",
                                                                defaultValue = "all") String stateParam,
                                                        @PositiveOrZero @RequestParam(name = "from",
                                                                defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size",
                                                                defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + stateParam));
        return bookingClient.getBookingsByBookerId(userId, state, from, size);
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> getBookingsByIdOwnerItem(@RequestHeader(HEADER_USER_ID) long userId,
                                                           @RequestParam(name = "state",
                                                                   defaultValue = "all") String stateParam,
                                                           @PositiveOrZero @RequestParam(name = "from",
                                                                   defaultValue = "0") Integer from,
                                                           @Positive @RequestParam(name = "size",
                                                                   defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + stateParam));
        return bookingClient.getBookingsByIdOwnerItem(userId, state, from, size);
    }
}
