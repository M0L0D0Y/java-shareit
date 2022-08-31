package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b " +
            " where b.bookerId = ?1" +
            " order by b.start desc ")
    List<Booking> findAllBookingsByBookerId(long bookerId);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.start > ?2" +
            " order by b.start desc ")
    List<Booking> findAllFutureBookingsByBookerId(long bookerId, LocalDateTime dateTime);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.end < ?2" +
            " order by b.start desc ")
    List<Booking> findAllPastBookingsByBookerId(long bookerId, LocalDateTime dateTime);


    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.status = ?2" +
            " order by b.start desc ")
    List<Booking> findByStatusAllBookingsByBookerId(long bookerId, Status status);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.start < ?2 and b.end > ?3" +
            " order by b.start desc ")
    List<Booking> findAllCurrentBookingsByBookerId(long bookerId,
                                                   LocalDateTime dateTimeOne,
                                                   LocalDateTime dateTimeTwo);

    List<Booking> findByItemId(long itemId);

    @Query("select b from Booking b " +
            " where b.itemId = ?1 and b.start > ?2" +
            " order by b.start desc ")
    List<Booking> findAllFutureBookingsByItemId(long itemId, LocalDateTime dateTime);

    @Query("select b from Booking b " +
            " where b.itemId = ?1 and b.end < ?2" +
            " order by b.start desc ")
    List<Booking> findAllPastBookingsByItemId(long itemId, LocalDateTime dateTime);

    @Query("select b from Booking b " +
            " where b.itemId = ?1 and b.status = ?2" +
            " order by b.start desc ")
    List<Booking> findByItemIdAndStatusIs(long itemId, Status status);

    @Query("select b from Booking b " +
            " where b.itemId = ?1 and b.start < ?2 and b.end > ?3" +
            " order by b.start desc ")
    List<Booking> findAllCurrentBookingByItemId(long itemId,
                                                LocalDateTime dateTimeOne,
                                                LocalDateTime dateTimeTwo);

    @Query("select b from Booking b " +
            " where b.itemId = ?1 and b.bookerId = ?2 and b.end < ?3" +
            " order by b.start desc ")
    List<Booking> findAllPastBookingsByBookerAndItemId(long itemId, long userId, LocalDateTime dateTime);
}

