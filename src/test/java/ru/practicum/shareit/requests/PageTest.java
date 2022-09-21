package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.UnavailableException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageTest {

    @Test
    void getPageableFalseFrom() {
        int from = -1;
        int size = 10;
        assertThrows(UnavailableException.class, () -> Page.getPageable(from, size));
    }

    @Test
    void getPageableFalseSize() {
        int from = 0;
        int size = 0;
        assertThrows(UnavailableException.class, () -> Page.getPageable(from, size));
    }

    @Test
    void getPageable() {
        int from = 1;
        int size = 1;
        assertEquals(PageRequest.of(1, size), Page.getPageable(from, size));
    }
}