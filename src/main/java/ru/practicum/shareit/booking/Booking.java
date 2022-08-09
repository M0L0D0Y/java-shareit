package ru.practicum.shareit.booking;

import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
public class Booking {
    Long id;
    @FutureOrPresent
    LocalDateTime start;
    @PastOrPresent
    LocalDateTime end;
    Long item;
    Long booker;
    String status;
}
