package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.ecxeption.ForbiddenException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.UserService;

import java.util.LinkedList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private static final String EMPTY_STRING = "";
    private static final String SPACE_STRING = " ";
    private final ItemStorage itemStorage;
    private final UserService userService;
    private final ItemIdGenerator generator;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage,
                           UserService userService,
                           ItemIdGenerator generator,
                           ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.generator = generator;
        this.itemMapper = itemMapper;
    }


    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        userService.getUser(userId);
        Item item = itemMapper.toItem(itemDto);
        item.setId(generator.getId());
        item.setOwner(userId);
        List<Item> items = new LinkedList<>(itemStorage.getAllItemsByIdOwner(userId));
        items.add(item);
        return itemMapper.toItemDto(itemStorage.addItem(userId, items));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        userService.getUser(userId);
        Item item = itemMapper.toItem(itemDto);
        Item updateItem = itemStorage.getItem(userId, itemId);
        if (updateItem.getOwner() != userId) {
            throw new ForbiddenException("Нет прав для изменения вещи");
        }
        List<Item> items = new LinkedList<>(itemStorage.getAllItemsByIdOwner(userId));
        items.remove(updateItem);
        String name = item.getName();
        String description = item.getDescription();
        Boolean available = item.getAvailable();
        if (name != null) {
            updateItem.setName(name);
        }
        if (description != null) {
            updateItem.setDescription(description);
        }
        if (available != null) {
            updateItem.setAvailable(available);
        }
        items.add(updateItem);
        return itemMapper.toItemDto(itemStorage.updateItem(userId, items));
    }

    @Override
    public ItemDto getItem(long userId, long id) {
        userService.getUser(userId);
        return itemMapper.toItemDto(itemStorage.getItem(userId, id));
    }

    @Override
    public List<ItemDto> getAllItemsByIdOwner(long userId) {
        userService.getUser(userId);
        List<Item> itemList = itemStorage.getAllItemsByIdOwner(userId);
        List<ItemDto> itemDtoList = new LinkedList<>();
        for (Item item : itemList) {
            itemDtoList.add(itemMapper.toItemDto(item));
        }
        return itemDtoList;

    }

    @Override
    public List<ItemDto> searchItem(long userId, String text) {
        userService.getUser(userId);
        if (text.equals(EMPTY_STRING) || text.equals(SPACE_STRING)) {
            return new LinkedList<>();
        }
        List<Item> itemList = itemStorage.searchItem(text);
        List<ItemDto> itemDtoList = new LinkedList<>();
        for (Item item : itemList) {
            itemDtoList.add(itemMapper.toItemDto(item));
        }
        return itemDtoList;
    }
}
