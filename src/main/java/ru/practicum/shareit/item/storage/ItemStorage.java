package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {
    //TODO переписать нанативный SQL
    List<Item> findItemByOwnerId(long ownerId);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(
            String name, String description);

}
