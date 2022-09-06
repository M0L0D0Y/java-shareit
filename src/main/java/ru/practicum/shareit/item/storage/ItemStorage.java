package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.List;

@Repository

public interface ItemStorage extends JpaRepository<Item, Long> {
    List<Item> findItemByOwnerId(long ownerId);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))" +
            " and i.available = true ")
    List<Item> searchItemByText(String text);

    @Query("select i from Item i where i.requestId = ?1 and  i.available = true ")
    List<Item> getAllItemByRequestId(long requestId);

}
