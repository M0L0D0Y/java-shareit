package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long> {

    //TODO переписать на нативный SQL
    List<Booking> findByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(long bookerId, LocalDateTime dateTime);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(long bookerId, LocalDateTime dateTime);

    List<Booking> findByBookerIdAndStatusIsOrderByStartDesc(long bookerId, Status status);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long bookerId,
                                                                              LocalDateTime dateTimeOne,
                                                                              LocalDateTime dateTimeTwo);

    List<Booking> findByItemId(long itemId);

    List<Booking> findByItemIdAndStartIsAfter(long itemId, LocalDateTime dateTime);

    List<Booking> findByItemIdAndEndIsBefore(long itemId, LocalDateTime dateTime);

    List<Booking> findByItemIdAndStatusIs(long itemId, Status status);

    List<Booking> findByItemIdAndStartIsBeforeAndEndIsAfter(long itemId,
                                                            LocalDateTime dateTimeOne,
                                                            LocalDateTime dateTimeTwo);

    List<Booking> findByItemIdAndBookerIdAndEndIsBefore(long itemId, long userId, LocalDateTime dateTime);
}

