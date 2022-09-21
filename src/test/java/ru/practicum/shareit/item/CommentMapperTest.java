package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.InputCommentDto;
import ru.practicum.shareit.item.dto.OutputCommentDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommentMapperTest {
    CommentMapper commentMapper;
    UserService userService;
    InputCommentDto inputCommentDto;
    Comment comment;
    OutputCommentDto outputCommentDto;
    User user;

    @BeforeEach
    void beforeEach() {
        userService = mock(UserServiceImpl.class);
        commentMapper = new CommentMapper(userService);
        inputCommentDto = new InputCommentDto("comment");
        comment = new Comment(1L, "comment", 1L, 1L, LocalDateTime.now());
        outputCommentDto = new OutputCommentDto(1L, "comment", "name", comment.getCreated());
        user = new User(1L, "name", "email@mail.ru");
    }

    @Test
    void toComment() {
        Comment comment1 = commentMapper.toComment(inputCommentDto);
        assertEquals(comment.getText(), comment1.getText());
    }

    @Test
    void toOutputCommentDto() {
        when(userService.getUser(anyLong()))
                .thenReturn(user);
        OutputCommentDto outputCommentDto1 = commentMapper.toOutputCommentDto(comment);
        assertEquals(outputCommentDto.getId(), outputCommentDto1.getId());
        assertEquals(outputCommentDto.getText(), outputCommentDto1.getText());
        assertEquals(outputCommentDto.getAuthorName(), outputCommentDto1.getAuthorName());
        assertEquals(outputCommentDto.getCreated(), outputCommentDto1.getCreated());
    }
}