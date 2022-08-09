package ru.practicum.shareit.ecxeption;

public class UserValidException extends RuntimeException {
    public UserValidException(String message) {
        super(message);
    }
}
