package ru.practicum.shareit.requests.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.OutputItemDto;
import ru.practicum.shareit.requests.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemRequestMapper {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemRequestMapper(ItemService itemService,
                             ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    public OutputItemRequestDto toOutputItemRequestDto(long userId, ItemRequest itemRequest) {
        OutputItemRequestDto outputItemRequestDto = new OutputItemRequestDto();
        outputItemRequestDto.setId(itemRequest.getId());
        outputItemRequestDto.setDescription(itemRequest.getDescription());
        outputItemRequestDto.setCreated(itemRequest.getCreated());
        List<OutputItemDto> itemDtoList = itemService.getAllItemByRequestId(userId, itemRequest.getId())
                .stream()
                .map(item -> itemMapper.toOutputItemDto(item, userId))
                .collect(Collectors.toList());
        outputItemRequestDto.setItems(itemDtoList);
        return outputItemRequestDto;
    }

    public ItemRequest toItemRequest(InputItemRequestDto inputItemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(inputItemRequestDto.getDescription());
        return itemRequest;
    }
}
