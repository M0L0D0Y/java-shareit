package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ItemStorage extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.owner.id =?1" +
            " order by i.id")
    List<Item> findItemByOwnerId(long ownerId, Pageable pageable);

    List<Item> findAllItemByOwnerId(long ownerId);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))" +
            " and i.available = true ")
    List<Item> searchItemByText(String text, Pageable pageable);

    @Query("select i from Item i where i.request.id = ?1 and  i.available = true ")
    List<Item> getAllItemByRequestId(long requestId);

}
