package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    Booking addBooking(long userId, Booking booking);

    Booking confirmBookingByOwner(long userId, long bookingId, boolean approved);

    Booking getBooking(long userId, long bookingId);

    List<Booking> getBookingsByBookerId(long userId, State state, int from, int size);

    List<Booking> getBookingsByIdOwnerItem(long userId, State state, int from, int size);

    List<Booking> getBookingsByItemId(long itemId);

    List<Booking> findAllPastBookingsByItemId(long itemId, LocalDateTime currentDateTime);

    List<Booking> findAllFutureBookingsByItemId(long itemId, LocalDateTime currentDateTime);

}
