package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    private static final byte FIRST_BOOKING_INDEX = 0;
    private static final byte MIN_SIZE = 1;

    private final BookingService bookingService;
    private final ItemService itemService;
    private final CommentMapper commentMapper;
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemMapper(BookingService bookingService,
                      ItemService itemService,
                      CommentMapper commentMapper,
                      ItemRequestService itemRequestService) {
        this.bookingService = bookingService;
        this.itemService = itemService;
        this.commentMapper = commentMapper;
        this.itemRequestService = itemRequestService;
    }

    public OutputItemDto toOutputItemDto(Item item, long userId) {
        OutputItemDto outputItemDto = new OutputItemDto();
        outputItemDto.setId(item.getId());
        outputItemDto.setName(item.getName());
        outputItemDto.setDescription(item.getDescription());
        outputItemDto.setAvailable(item.getAvailable());
        ItemRequest itemRequest = item.getRequest();
        if (itemRequest != null) {
            outputItemDto.setRequestId(item.getRequest().getId());
        }
        if (userId == item.getOwner().getId()) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            addLastBooking(outputItemDto, item.getId(), currentDateTime);
            addNextBooking(outputItemDto, item.getId(), currentDateTime);
            return outputItemDto;
        }
        return outputItemDto;
    }

    public Item toItem(Long userId, InputItemDto inputItemDto) {
        Item item = new Item();
        item.setName(inputItemDto.getName());
        item.setDescription(inputItemDto.getDescription());
        item.setAvailable(inputItemDto.getAvailable());
        if ((inputItemDto.getRequestId() != null) && (inputItemDto.getRequestId() > 0)) {
            ItemRequest itemRequest = itemRequestService.getItemRequestById(userId, inputItemDto.getRequestId());
            item.setRequest(itemRequest);
        } else {
            item.setRequest(null);
        }
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
            List<OutputCommentDto> outputCommentDtoList = comments
                    .stream()
                    .map(commentMapper::toOutputCommentDto)
                    .collect(Collectors.toList());
            outputItemDtoWithComment.setComments(outputCommentDtoList);
            return outputItemDtoWithComment;
        }
        outputItemDtoWithComment.setComments(new ArrayList<>());
        return outputItemDtoWithComment;

    }

    private void addLastBooking(OutputItemDto outputItemDto, long itemId, LocalDateTime currentDateTime) {
        List<Booking> bookings = bookingService.getBookingsByItemId(itemId);
        if (!bookings.isEmpty()) {
            List<Booking> allLastBookings = bookingService.findAllPastBookingsByItemId(itemId, currentDateTime);
            if (!allLastBookings.isEmpty()) {
                BookingDto lastBooking = new BookingDto();
                lastBooking.setId(allLastBookings.get(FIRST_BOOKING_INDEX).getId());
                lastBooking.setBookerId(allLastBookings.get(FIRST_BOOKING_INDEX).getBookerId());
                outputItemDto.setLastBooking(lastBooking);
            }
        }
    }

    private void addNextBooking(OutputItemDto outputItemDto, long itemId, LocalDateTime currentDateTime) {
        List<Booking> bookings = bookingService.getBookingsByItemId(itemId);
        if (!bookings.isEmpty()) {
            List<Booking> allNextBookings = bookingService.findAllFutureBookingsByItemId(itemId, currentDateTime);
            if (!allNextBookings.isEmpty()) {
                BookingDto nextBooking = new BookingDto();
                int lastElement = allNextBookings.size() - MIN_SIZE;
                nextBooking.setId(allNextBookings.get(lastElement).getId());
                nextBooking.setBookerId(allNextBookings.get(lastElement).getBookerId());
                outputItemDto.setNextBooking(nextBooking);
            }
        }
    }
}
