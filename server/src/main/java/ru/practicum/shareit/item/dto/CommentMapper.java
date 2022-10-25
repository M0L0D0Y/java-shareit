package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

@Component
public class CommentMapper {
    private final UserService userService;

    @Autowired
    public CommentMapper(UserService userService) {
        this.userService = userService;
    }

    public Comment toComment(InputCommentDto inputCommentDto) {
        Comment comment = new Comment();
        comment.setText(inputCommentDto.getText());
        return comment;
    }

    public OutputCommentDto toOutputCommentDto(Comment comment) {
        OutputCommentDto outputCommentDto = new OutputCommentDto();
        outputCommentDto.setId(comment.getId());
        User user = userService.getUser(comment.getAuthorId());
        outputCommentDto.setAuthorName(user.getName());
        outputCommentDto.setText(comment.getText());
        outputCommentDto.setCreated(comment.getCreated());
        return outputCommentDto;
    }
}
