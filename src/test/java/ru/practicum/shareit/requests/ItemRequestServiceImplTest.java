package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemRequestServiceImplTest {
    ItemRequestService itemRequestService;
    ItemRequestStorage itemRequestStorage;
    UserStorage userStorage;
    User user1;
    ItemRequest itemRequest;

    @BeforeEach
    void beforeEach() {
        userStorage = mock(UserStorage.class);
        itemRequestStorage = mock(ItemRequestStorage.class);
        itemRequestService = new ItemRequestServiceImpl(itemRequestStorage, userStorage);
        user1 = new User(1L, "user1", "user1@mail.ru");
        itemRequest = new ItemRequest(1L, "text", user1.getId(), LocalDateTime.now());
    }

    @Test
    void addItemRequestNullCreated() {
        itemRequest.setCreated(null);
        when(userStorage.findById(user1.getId()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemRequestStorage.save(itemRequest))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> itemRequestService.addItemRequest(user1.getId(), itemRequest));
    }

    @Test
    void addItemRequestNullRequestor() {
        itemRequest.setRequestor(null);
        when(userStorage.findById(user1.getId()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemRequestStorage.save(itemRequest))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> itemRequestService.addItemRequest(user1.getId(), itemRequest));
    }

    @Test
    void addItemRequestNullDescription() {
        itemRequest.setDescription(null);
        when(userStorage.findById(user1.getId()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemRequestStorage.save(itemRequest))
                .thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> itemRequestService.addItemRequest(user1.getId(), itemRequest));
    }

    @Test
    void addItemRequestNotExistUserId() {
        when(userStorage.findById(user1.getId()))
                .thenThrow(new NotFoundException("Пользователя с таким id нет " + user1.getId()));
        assertThrows(NotFoundException.class, () -> itemRequestService.addItemRequest(user1.getId(), itemRequest));
    }

    @Test
    void addItemRequest() {
        when(userStorage.findById(user1.getId()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemRequestStorage.save(itemRequest))
                .thenReturn(itemRequest);
        final ItemRequest savedItemRequest = itemRequestService.addItemRequest(user1.getId(), itemRequest);
        assertNotNull(savedItemRequest);
        assertEquals(itemRequest.getId(), savedItemRequest.getId());
        assertEquals(itemRequest.getDescription(), savedItemRequest.getDescription());
        assertEquals(itemRequest.getRequestor(), savedItemRequest.getRequestor());
        assertEquals(itemRequest.getCreated(), savedItemRequest.getCreated());
    }

    @Test
    void getAllMeRequestNotExistUserId() {
        when(userStorage.findById(user1.getId()))
                .thenThrow(new NotFoundException("Пользователя с таким id нет " + user1.getId()));
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllMeRequest(user1.getId()));
    }

    @Test
    void getAllMeRequestFailUserId() {
        when(userStorage.findById(user1.getId()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemRequestStorage.getAllMeRequest(user1.getId()))
                .thenReturn(List.of());
        final List<ItemRequest> requests = itemRequestService.getAllMeRequest(user1.getId());
        assertNotNull(requests);
        assertEquals(0, requests.size());
    }

    @Test
    void getAllMeRequest() {
        when(userStorage.findById(user1.getId()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemRequestStorage.getAllMeRequest(user1.getId()))
                .thenReturn(List.of(itemRequest));
        final List<ItemRequest> requests = itemRequestService.getAllMeRequest(user1.getId());
        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertEquals(itemRequest.getId(), requests.get(0).getId());
        assertEquals(itemRequest.getDescription(), requests.get(0).getDescription());
        assertEquals(itemRequest.getRequestor(), requests.get(0).getRequestor());
        assertEquals(itemRequest.getCreated(), requests.get(0).getCreated());
    }

    @Test
    void getAllItemRequestNotExistUserId() {
        when(userStorage.findById(user1.getId()))
                .thenThrow(new NotFoundException("Пользователя с таким id нет " + user1.getId()));
        assertThrows(NotFoundException.class, () -> itemRequestService.getAllMeRequest(user1.getId()));
    }

    @Test
    void getAllItemRequestFailUserId() {
        when(userStorage.findById(user1.getId()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemRequestStorage.getAllRequest(user1.getId(), Pageable.ofSize(10)))
                .thenReturn(List.of());
        final List<ItemRequest> requests = itemRequestService.getAllItemRequest(user1.getId(), 0, 10);
        assertNotNull(requests);
        assertEquals(0, requests.size());
    }

    @Test
    void getAllItemRequest() {
        when(userStorage.findById(user1.getId()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemRequestStorage.getAllRequest(user1.getId(), Pageable.ofSize(10)))
                .thenReturn(List.of(itemRequest));
        final List<ItemRequest> requests = itemRequestService.getAllItemRequest(user1.getId(), 0, 10);
        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertEquals(itemRequest.getId(), requests.get(0).getId());
        assertEquals(itemRequest.getDescription(), requests.get(0).getDescription());
        assertEquals(itemRequest.getRequestor(), requests.get(0).getRequestor());
        assertEquals(itemRequest.getCreated(), requests.get(0).getCreated());
    }

    @Test
    void getItemRequestByIdNotExistUserId() {
        when(userStorage.findById(user1.getId()))
                .thenThrow(new NotFoundException("Пользователя с таким id нет " + user1.getId()));
        assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequestById(user1.getId(), itemRequest.id));
    }

    @Test
    void getItemRequestByIdNotExistRequestId() {
        when(userStorage.findById(user1.getId()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemRequestStorage.findById(itemRequest.getId()))
                .thenThrow(new NotFoundException("Нет запроса с таким id " + itemRequest.getId()));
        assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequestById(user1.getId(), itemRequest.id));
    }

    @Test
    void getItemRequestById() {
        when(userStorage.findById(user1.getId()))
                .thenReturn(Optional.ofNullable(user1));
        when(itemRequestStorage.findById(itemRequest.getId()))
                .thenReturn(Optional.ofNullable(itemRequest));
        final ItemRequest request = itemRequestService.getItemRequestById(user1.getId(), itemRequest.id);
        assertNotNull(request);
        assertEquals(itemRequest.getId(), request.getId());
        assertEquals(itemRequest.getDescription(), request.getDescription());
        assertEquals(itemRequest.getRequestor(), request.getRequestor());
        assertEquals(itemRequest.getCreated(), request.getCreated());
    }
}