package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.InputItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.OutputItemRequestDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService,
                                 ItemRequestMapper itemRequestMapper) {
        this.itemRequestService = itemRequestService;
        this.itemRequestMapper = itemRequestMapper;
    }


    @PostMapping
    public OutputItemRequestDto addItemRequest(@RequestHeader(HEADER_USER_ID) Long userId,
                                               @Valid @RequestBody InputItemRequestDto inputItemRequestDto) {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(inputItemRequestDto);
        return itemRequestMapper.toOutputItemRequestDto(userId, itemRequestService.addItemRequest(userId, itemRequest));
    }

    @GetMapping
    public List<OutputItemRequestDto> getAllMeItemRequest(@RequestHeader(HEADER_USER_ID) Long userId) {
        return itemRequestService.getAllMeRequest(userId)
                .stream()
                .map(itemRequest -> itemRequestMapper.toOutputItemRequestDto(userId, itemRequest))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/all")
    public List<OutputItemRequestDto> getAllItemRequest(@RequestHeader(HEADER_USER_ID) Long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        return itemRequestService.getAllItemRequest(userId, from, size)
                .stream()
                .map(itemRequest -> itemRequestMapper.toOutputItemRequestDto(userId, itemRequest))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{requestId}")
    public OutputItemRequestDto getItemRequestById(@RequestHeader(HEADER_USER_ID) Long userId,
                                                   @PathVariable long requestId) {
        return itemRequestMapper.toOutputItemRequestDto(userId,
                itemRequestService.getItemRequestById(userId, requestId));

    }
}
