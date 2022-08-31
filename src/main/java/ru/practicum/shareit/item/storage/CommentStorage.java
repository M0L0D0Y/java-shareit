package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Comment;

import java.util.List;

@Repository
public interface CommentStorage extends JpaRepository<Comment, Long> {
    List<Comment> findByItemId(long itemId);

}
