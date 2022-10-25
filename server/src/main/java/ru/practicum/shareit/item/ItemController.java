package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemController(ItemService itemService,
                          ItemMapper itemMapper,
                          CommentMapper commentMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.commentMapper = commentMapper;
    }


    @PostMapping
    public OutputItemDto addItem(@RequestHeader(HEADER_USER_ID) Long userId,
                                 @RequestBody InputItemDto inputItemDto) {
        Item item = itemMapper.toItem(userId, inputItemDto);
        return itemMapper.toOutputItemDto(itemService.addItem(userId, item), userId);
    }

    @PatchMapping(value = "/{itemId}")
    public OutputItemDto updateItem(@RequestHeader(HEADER_USER_ID) Long userId,
                                    @PathVariable Long itemId,
                                    @RequestBody InputItemDto itemDto) {
        Item item = itemMapper.toItem(userId, itemDto);
        return itemMapper.toOutputItemDto(itemService.updateItem(userId, itemId, item), userId);
    }

    @GetMapping(value = "/{itemId}")
    public OutputItemDtoWithComment getItem(@RequestHeader(HEADER_USER_ID) Long userId, @PathVariable Long itemId) {
        OutputItemDto outputItemDto = itemMapper.toOutputItemDto(itemService.getItem(userId, itemId), userId);
        return itemMapper.toOutputItemDtoWithComment(outputItemDto);
    }

    @GetMapping
    public List<OutputItemDtoWithComment> getAllItem(@RequestHeader(HEADER_USER_ID) Long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        return itemService.getAllItem(userId, from, size)
                .stream()
                .map(item -> itemMapper.toOutputItemDto(item, userId))
                .map(itemMapper::toOutputItemDtoWithComment)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/search")
    public List<OutputItemDto> searchItemByText(@RequestHeader(HEADER_USER_ID) Long userId,
                                                @RequestParam String text,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return itemService.searchItemByText(userId, text, from, size)
                .stream()
                .map(item -> itemMapper.toOutputItemDto(item, userId))
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/{itemId}/comment")
    public OutputCommentDto addComment(@RequestHeader(HEADER_USER_ID) Long userId,
                                       @PathVariable Long itemId,
                                       @RequestBody InputCommentDto inputCommentDto) {
        Comment comment = commentMapper.toComment(inputCommentDto);
        return commentMapper.toOutputCommentDto(itemService.addComment(userId, itemId, comment));
    }
}
