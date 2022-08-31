package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    private static final byte FIRST_ELEMENT = 0;
    private static final byte MIN_SIZE = 1;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemMapper(BookingService bookingService,
                      ItemService itemService,
                      CommentMapper commentMapper) {
        this.bookingService = bookingService;
        this.itemService = itemService;
        this.commentMapper = commentMapper;
    }

    public OutputItemDto toOutputItemDto(Item item, long userId) {
        OutputItemDto outputItemDto = new OutputItemDto();
        outputItemDto.setId(item.getId());
        outputItemDto.setName(item.getName());
        outputItemDto.setDescription(item.getDescription());
        outputItemDto.setAvailable(item.getAvailable());
        if (userId == item.getOwnerId()) {
            LocalDateTime dateTime = LocalDateTime.now();
            addLastBooking(outputItemDto, item.getId(), dateTime);
            addNextBooking(outputItemDto, item.getId(), dateTime);
            return outputItemDto;
        }
        return outputItemDto;
    }

    public Item toItem(InputItemDto inputItemDto) {
        Item item = new Item();
        item.setName(inputItemDto.getName());
        item.setDescription(inputItemDto.getDescription());
        item.setAvailable(inputItemDto.getAvailable());
        return item;
    }

    public OutputItemDtoWithComment toOutputItemDtoWithComment(OutputItemDto outputItemDto) {
        OutputItemDtoWithComment outputItemDtoWithComment = new OutputItemDtoWithComment();
        outputItemDtoWithComment.setId(outputItemDto.getId());
        outputItemDtoWithComment.setName(outputItemDto.getName());
        outputItemDtoWithComment.setDescription(outputItemDto.getDescription());
        outputItemDtoWithComment.setAvailable(outputItemDto.getAvailable());
        if (outputItemDto.getLastBooking() != null) {
            outputItemDtoWithComment.setLastBooking(outputItemDto.getLastBooking());
        }
        if (outputItemDto.getNextBooking() != null) {
            outputItemDtoWithComment.setNextBooking(outputItemDto.getNextBooking());
        }
        List<Comment> comments = itemService.getCommentsByItemID(outputItemDto.getId());
        if (!comments.isEmpty()) {
            comments.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
        }
        List<OutputCommentDto> outputCommentDtoList = comments
                .stream()
                .map(commentMapper::toOutputCommentDto)
                .collect(Collectors.toList());
        outputItemDtoWithComment.setComments(outputCommentDtoList);
        return outputItemDtoWithComment;

    }

    private void addLastBooking(OutputItemDto outputItemDto, long itemId, LocalDateTime dateTime) {
        List<Booking> bookings = bookingService.getBookingsByItemId(itemId);
        if (!bookings.isEmpty()) {
            List<Booking> allLastBookings = bookingService.findAllPastBookingsByItemId(itemId, dateTime);
            if (!allLastBookings.isEmpty()) {
                if (allLastBookings.size() > MIN_SIZE) {
                    allLastBookings.sort(Comparator.comparing(Booking::getEnd));
                }
                LastBooking lastBooking = new LastBooking();
                lastBooking.setId(allLastBookings.get(FIRST_ELEMENT).getId());
                lastBooking.setBookerId(allLastBookings.get(FIRST_ELEMENT).getBookerId());
                outputItemDto.setLastBooking(lastBooking);
            }
        }
    }

    private void addNextBooking(OutputItemDto outputItemDto, long itemId, LocalDateTime dateTime) {
        List<Booking> bookings = bookingService.getBookingsByItemId(itemId);
        if (!bookings.isEmpty()) {
            List<Booking> allNextBookings = bookingService.findAllFutureBookingsByItemId(itemId, dateTime);
            if (!allNextBookings.isEmpty()) {
                if (bookings.size() > MIN_SIZE) {
                    bookings.sort((o1, o2) -> o2.getStart().compareTo(o1.getStart()));
                }
                NextBooking nextBooking = new NextBooking();
                nextBooking.setId(allNextBookings.get(FIRST_ELEMENT).getId());
                nextBooking.setBookerId(allNextBookings.get(FIRST_ELEMENT).getBookerId());
                outputItemDto.setNextBooking(nextBooking);
            }
        }
    }
}
