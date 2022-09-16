package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableException;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemServiceImplTest {

    ItemService itemService;
    ItemStorage itemStorage;
    UserStorage userStorage;
    CommentStorage commentStorage;
    BookingStorage bookingStorage;
    ItemRequestStorage itemRequestStorage;
    User user1;
    User user2;
    Item item1;
    Item item2;
    ItemRequest itemRequest;
    Comment comment;
    Booking booking;


    @BeforeEach
    void beforeEach() {
        userStorage = mock(UserStorage.class);
        itemStorage = mock(ItemStorage.class);
        commentStorage = mock(CommentStorage.class);
        bookingStorage = mock(BookingStorage.class);
        itemRequestStorage = mock(ItemRequestStorage.class);
        itemService = new ItemServiceImpl(itemStorage, userStorage, commentStorage, bookingStorage);
        user1 = new User(1L, "user1", "user1@mail.ru");
        item1 = new Item(1L, "item1", "description1", true, user1.getId(), null);
        itemRequest = new ItemRequest(1L, "description", user1.getId(), LocalDateTime.now());
        user2 = new User(2L, "user2", "user2@mail.ru");
        item2 = new Item(2L, "item2", "description2", false, user2.getId(), itemRequest.getId());
        booking = new Booking(1L,
                LocalDateTime.of(2022, 8, 1, 12, 0),
                LocalDateTime.of(2022, 8, 2, 12, 0),
                item1.getId(), user2.getId(), Status.APPROVED);
        comment = new Comment(1L, "text", item1.getId(), user2.getId(),
                LocalDateTime.now());
    }

    @Test
    void addItemNullRequestId() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.save(any(Item.class)))
                .thenReturn(item1);
        final Item savedItem = itemService.addItem(user1.getId(), item1);
        assertNotNull(savedItem);
        assertEquals(item1.getId(), savedItem.getId());
        assertEquals(item1.getName(), savedItem.getName());
        assertEquals(item1.getDescription(), savedItem.getDescription());
        assertEquals(item1.getAvailable(), savedItem.getAvailable());
        assertEquals(item1.getOwnerId(), savedItem.getOwnerId());
    }

    @Test
    void addItem() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.save(any(Item.class)))
                .thenReturn(item2);
        final Item savedItem = itemService.addItem(user1.getId(), item2);
        assertNotNull(savedItem);
        assertEquals(item2.getId(), savedItem.getId());
        assertEquals(item2.getName(), savedItem.getName());
        assertEquals(item2.getDescription(), savedItem.getDescription());
        assertEquals(item2.getAvailable(), savedItem.getAvailable());
        assertEquals(item2.getOwnerId(), savedItem.getOwnerId());
        assertEquals(item2.getRequestId(), savedItem.getRequestId());
    }

    @Test
    void updateItemAvailable() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        when(itemStorage.save(any(Item.class)))
                .thenReturn(item2);

        final Item updatedItem = itemService.updateItem(user1.getId(), item1.getId(), item2);
        assertNotNull(updatedItem);
        assertEquals(item2.getId(), updatedItem.getId());
        assertEquals(item2.getAvailable(), updatedItem.getAvailable());
    }

    @Test
    void updateItemDescription() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        when(itemStorage.save(any(Item.class)))
                .thenReturn(item2);

        final Item updatedItem = itemService.updateItem(user1.getId(), item1.getId(), item2);
        assertNotNull(updatedItem);
        assertEquals(item2.getId(), updatedItem.getId());
        assertEquals(item2.getDescription(), updatedItem.getDescription());
    }

    @Test
    void updateItemName() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        when(itemStorage.save(any(Item.class)))
                .thenReturn(item2);

        final Item updatedItem = itemService.updateItem(user1.getId(), item1.getId(), item2);
        assertNotNull(updatedItem);
        assertEquals(item2.getId(), updatedItem.getId());
        assertEquals(item2.getName(), updatedItem.getName());
    }

    @Test
    void updateItemNoRights() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item2));

        assertThrows(ForbiddenException.class, () -> itemService.updateItem(user1.getId(), item1.getId(), item2));
    }

    @Test
    void updateItemFailUserId() {
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> itemService.updateItem(user1.getId(), item1.getId(), item2));
    }

    @Test
    void updateItemFailItemId() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> itemService.updateItem(user1.getId(), item1.getId(), item2));
    }

    @Test
    void updateItem() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        when(itemStorage.save(any(Item.class)))
                .thenReturn(item2);

        final Item updatedItem = itemService.updateItem(user1.getId(), item1.getId(), item2);
        assertNotNull(updatedItem);
        assertEquals(item2.getId(), updatedItem.getId());
        assertEquals(item2.getName(), updatedItem.getName());
        assertEquals(item2.getDescription(), updatedItem.getDescription());
        assertEquals(item2.getAvailable(), updatedItem.getAvailable());
        assertEquals(item2.getOwnerId(), updatedItem.getOwnerId());
    }

    @Test
    void getItemFailItemId() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> itemService.getItem(1L, item1.getId()));
    }

    @Test
    void getItemFailUserId() {
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> itemService.getItem(1L, item1.getId()));
    }

    @Test
    void getItem() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item2));
        final Item foundItem = itemService.getItem(user2.getId(), item2.getId());
        assertNotNull(foundItem);
        assertEquals(item2.getId(), foundItem.getId());
        assertEquals(item2.getName(), foundItem.getName());
        assertEquals(item2.getDescription(), foundItem.getDescription());
        assertEquals(item2.getAvailable(), foundItem.getAvailable());
        assertEquals(item2.getOwnerId(), foundItem.getOwnerId());
        assertEquals(item2.getRequestId(), foundItem.getRequestId());
    }

    @Test
    void getAllItemFailParamSize() {
        int from = 0;
        int size = -10;
        when(userStorage.findById(anyLong()))
                .thenThrow(UnavailableException.class);
        assertThrows(UnavailableException.class, () -> itemService.getAllItem(anyLong(), from, size));
    }

    @Test
    void getAllItemFailParamFrom() {
        int from = -2;
        int size = 10;
        when(userStorage.findById(anyLong()))
                .thenThrow(UnavailableException.class);
        assertThrows(UnavailableException.class, () -> itemService.getAllItem(anyLong(), from, size));
    }

    @Test
    void getAllItemFailUserId() {
        int from = 0;
        int size = 10;
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> itemService.getAllItem(1L, from, size));
    }

    @Test
    void getAllItem() {
        int from = 0;
        int size = 10;
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));
        when(itemStorage.findItemByOwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(List.of(item2));
        final List<Item> foundItems = itemService.getAllItem(user2.getId(), from, size);
        assertNotNull(foundItems);
        assertEquals(item2.getId(), foundItems.get(0).getId());
        assertEquals(item2.getName(), foundItems.get(0).getName());
        assertEquals(item2.getDescription(), foundItems.get(0).getDescription());
        assertEquals(item2.getAvailable(), foundItems.get(0).getAvailable());
        assertEquals(item2.getOwnerId(), foundItems.get(0).getOwnerId());
        assertEquals(item2.getRequestId(), foundItems.get(0).getRequestId());
    }

    @Test
    void searchItemByTextFailParamSize() {
        int from = 0;
        int size = -10;
        when(userStorage.findById(anyLong()))
                .thenThrow(UnavailableException.class);
        assertThrows(UnavailableException.class, () -> itemService.getAllItem(anyLong(), from, size));
    }

    @Test
    void searchItemByTextFailParamFrom() {
        int from = -2;
        int size = 10;
        when(userStorage.findById(anyLong()))
                .thenThrow(UnavailableException.class);
        assertThrows(UnavailableException.class, () -> itemService.getAllItem(anyLong(), from, size));
    }

    @Test
    void searchItemByFailText() {
        int from = 0;
        int size = 10;
        String text = " ";
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));
        final List<Item> items = itemService.searchItemByText(user2.getId(), text, from, size);
        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    void searchItemByText() {
        int from = 0;
        int size = 10;
        String text = "desc";
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));
        when(itemStorage.searchItemByText(anyString(), any(Pageable.class)))
                .thenReturn(List.of(item2));
        final List<Item> items = itemService.searchItemByText(user2.getId(), text, from, size);
        assertNotNull(items);
        assertEquals(item2.getId(), items.get(0).getId());
        assertEquals(item2.getName(), items.get(0).getName());
        assertEquals(item2.getDescription(), items.get(0).getDescription());
        assertEquals(item2.getAvailable(), items.get(0).getAvailable());
        assertEquals(item2.getOwnerId(), items.get(0).getOwnerId());
        assertEquals(item2.getRequestId(), items.get(0).getRequestId());
    }

    @Test
    void addCommentFailItemId() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> itemService.addComment(user1.getId(), 1L, comment));
    }

    @Test
    void addCommentFailUserId() {
        when(userStorage.findById(anyLong()))
                .thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> itemService.addComment(1L, item2.getId(), comment));

    }

    @Test
    void addCommentUnavailable() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item2));
        when(bookingStorage.findAllPastBookingsByBookerAndItemId(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(commentStorage.save(comment))
                .thenThrow(UnavailableException.class);
        assertThrows(UnavailableException.class, () -> itemService.addComment(user1.getId(), item2.getId(), comment));
    }

    @Test
    void addCommit() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        when(bookingStorage.findAllPastBookingsByBookerAndItemId(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(commentStorage.save(comment))
                .thenReturn(comment);
        final Comment savedCommit = itemService.addComment(user1.getId(), item1.getId(), comment);
        assertNotNull(savedCommit);
        assertEquals(comment.getId(), savedCommit.getId());
        assertEquals(comment.getText(), savedCommit.getText());
        assertEquals(comment.getItemId(), savedCommit.getItemId());
        assertEquals(comment.getAuthorId(), savedCommit.getAuthorId());
        assertEquals(comment.getCreated(), savedCommit.getCreated());
    }

    @Test
    void getCommentsByFalseItemID() {
        when(commentStorage.findByItemId(anyLong()))
                .thenReturn(List.of());
        List<Comment> comments = itemService.getCommentsByItemID(1L);
        assertNotNull(comments);
        assertEquals(0, comments.size());
    }

    @Test
    void getCommentsByItemID() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));
        when(itemStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item1));
        when(commentStorage.findByItemId(anyLong()))
                .thenReturn(List.of(comment));
        final List<Comment> foundCommits = itemService.getCommentsByItemID(item1.getId());
        assertNotNull(foundCommits);
        assertEquals(1, foundCommits.size());
        assertEquals(comment.getId(), foundCommits.get(0).getId());
        assertEquals(comment.getAuthorId(), foundCommits.get(0).getAuthorId());
        assertEquals(comment.getItemId(), foundCommits.get(0).getItemId());
        assertEquals(comment.getText(), foundCommits.get(0).getText());
        assertEquals(comment.getCreated(), foundCommits.get(0).getCreated());
    }

    @Test
    void getAllItemByRequestId() {
        when(userStorage.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));
        when(itemStorage.getAllItemByRequestId(anyLong()))
                .thenReturn(List.of(item2));
        final List<Item> items = itemService.getAllItemByRequestId(user2.getId(), itemRequest.getId());
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item2.getId(), items.get(0).getId());
        assertEquals(item2.getName(), items.get(0).getName());
        assertEquals(item2.getDescription(), items.get(0).getDescription());
        assertEquals(item2.getRequestId(), items.get(0).getRequestId());
        assertEquals(item2.getAvailable(), items.get(0).getAvailable());
        assertEquals(item2.getOwnerId(), items.get(0).getOwnerId());
    }
}