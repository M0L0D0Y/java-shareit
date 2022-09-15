package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
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
    List<Booking> findAllBookingsByBookerId(long bookerId, Pageable pageable);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.start > ?2" +
            " order by b.start desc ")
    List<Booking> findAllFutureBookingsByBookerId(long bookerId, LocalDateTime currentDateTime, Pageable pageable);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.end < ?2" +
            " order by b.start desc ")
    List<Booking> findAllPastBookingsByBookerId(long bookerId, LocalDateTime currentDateTime, Pageable pageable);


    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.status = ?2" +
            " order by b.start desc ")
    List<Booking> findByStatusAllBookingsByBookerId(long bookerId, Status status, Pageable pageable);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.start < ?2 and b.end > ?2" +
            " order by b.start desc ")
    List<Booking> findAllCurrentBookingsByBookerId(long bookerId,
                                                   LocalDateTime dateTime,
                                                   Pageable pageable);

    @Query("select b from Booking b " +
            " where b.itemId in(select i.id from Item i where i.ownerId = ?1)" +
            " order by b.start desc ")
    List<Booking> findByIdOwnerItem(long userId, Pageable pageable);

    @Query("select b from Booking b " +
            " where b.itemId in(select i.id from Item i where i.ownerId = ?1)" +
            " and b.end < ?2" +
            " order by b.start desc ")
    List<Booking> findAllPastBookingsByIdOwnerItem(long userId, LocalDateTime currentDateTime, Pageable pageable);

    @Query("select b from Booking b " +
            " where b.itemId in(select i.id from Item i where i.ownerId = ?1)" +
            " and b.start > ?2" +
            " order by b.start desc ")
    List<Booking> findAllFutureBookingsByIdOwnerItem(long userId, LocalDateTime currentDateTime, Pageable pageable);

    @Query("select b from Booking b " +
            " where b.itemId in(select i.id from Item i where i.ownerId = ?1)" +
            " and b.status = ?2" +
            " order by b.start desc ")
    List<Booking> findByIdOwnerItemAndStatusIs(long userId, Status status, Pageable pageable);

    @Query("select b from Booking b " +
            " where b.itemId in(select i.id from Item i where i.ownerId = ?1)" +
            " and b.start < ?2 and b.end > ?2" +
            " order by b.start desc ")
    List<Booking> findAllCurrentBookingByIdOwnerItem(long itemId,
                                                     LocalDateTime currentDateTime,
                                                     Pageable pageable);

    List<Booking> findByItemId(long itemId);

    @Query("select b from Booking b " +
            " where b.itemId = ?1 and b.start > ?2" +
            " order by b.start desc ")
    List<Booking> findAllFutureBookingsByItemId(long itemId, LocalDateTime currentDateTime);

    @Query("select b from Booking b " +
            " where b.itemId = ?1 and b.end < ?2" +
            " order by b.start desc ")
    List<Booking> findAllPastBookingsByItemId(long itemId, LocalDateTime currentDateTime);

    @Query("select b from Booking b " +
            " where b.itemId = ?1 and b.bookerId = ?2 and b.end < ?3" +
            " order by b.start desc ")
    List<Booking> findAllPastBookingsByBookerAndItemId(long itemId, long userId, LocalDateTime currentDateTime);

}

