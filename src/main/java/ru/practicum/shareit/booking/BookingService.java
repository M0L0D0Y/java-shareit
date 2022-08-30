package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    Booking addBooking(long userId, Booking booking);

    Booking replyFromOwner(long userId, long bookingId, boolean approved);

    Booking getBooking(long userId, long bookingId);

    List<Booking> getBookingsBooker(long userId, String state);

    List<Booking> getBookingsOwner(long userId, String state);

    List<Booking> getBookingsByItemId(long itemId);

    List<Booking> getBookingsByItemIdForPastState(long itemId, LocalDateTime dateTime);

    List<Booking> getBookingsByItemIdForFutureState(long itemId, LocalDateTime dateTime);

    List<Booking> getBookingsUsedByUser(long itemId, long userId, LocalDateTime dateTime);

}
