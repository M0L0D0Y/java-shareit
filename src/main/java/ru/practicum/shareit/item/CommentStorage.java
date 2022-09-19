package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentStorage extends JpaRepository<Comment, Long> {
    List<Comment> findByItemIdOrderById(long itemId);

}
