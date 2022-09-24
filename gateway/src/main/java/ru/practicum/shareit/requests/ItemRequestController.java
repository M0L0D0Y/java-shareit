package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.InputItemRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(HEADER_USER_ID) Long userId,
                                                 @RequestBody @Valid InputItemRequest inputItemRequest) {
        return itemRequestClient.addItemRequest(userId, inputItemRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getAllMeItemRequest(@RequestHeader(HEADER_USER_ID) @Positive Long userId) {
        return itemRequestClient.getAllMeItemRequest(userId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAllItemRequest(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                                    @PositiveOrZero @RequestParam(name = "from",
                                                            defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size",
                                                            defaultValue = "10") Integer size) {
        return itemRequestClient.getAllItemRequest(userId, from, size);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(HEADER_USER_ID) @Positive Long userId,
                                                     @PathVariable @Positive Long requestId) {
        return itemRequestClient.getItemRequestById(userId, requestId);

    }

}
