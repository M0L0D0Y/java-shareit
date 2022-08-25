package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(long bookerId, LocalDateTime dateTime);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(long bookerId, LocalDateTime dateTime);

    List<Booking> findByBookerIdAndStatusIsOrderByStartDesc(long bookerId, Status status);

    List<Booking> findByItemId(long itemId);

    List<Booking> findByItemIdAndStartIsAfter(long itemId, LocalDateTime dateTime);

    List<Booking> findByItemIdAndEndIsBefore(long itemId, LocalDateTime dateTime);

    List<Booking> findByItemIdAndStatusIs(long itemId, Status status);
}

