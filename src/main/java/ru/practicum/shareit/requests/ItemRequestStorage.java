package ru.practicum.shareit.requests;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ItemRequestStorage extends PagingAndSortingRepository<ItemRequest, Long> {
    @Query("select r from ItemRequest r where r.requestor = ?1" +
            " order by r.created desc ")
    List<ItemRequest> getAllMeRequest(long userId);

    @Query("select r from ItemRequest r" +
            " order by r.created desc ")
    List<ItemRequest> getAllRequest(long userId, Pageable pageable);
}
