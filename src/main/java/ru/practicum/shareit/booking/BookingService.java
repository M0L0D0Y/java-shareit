package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(long userId, Booking booking);

    Booking approveOwner(long userId, Long bookingId, boolean approved);

    Booking getBooking(long userId, long bookingId);

    List<Booking> getBookingsByBooker(long userId, String state);

    List<Booking> getBookingsByOwner(long userId, String state);
}
