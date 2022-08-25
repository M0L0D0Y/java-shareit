package ru.practicum.shareit.booking;

public enum Status {
    WAITING,//НОВОЕ БРОНИРОВАНИЕ, ОЖИДАЕТ ПОДТВЕРЖДЕНИЯ
    APPROVED,// ПОДТВЕРЖДЕНО ВЛАДЕЛЬЦЕМ
    REJECTED,//ОТКЛОНЕНО ВЛАДЕЛЬЦЕМ
    CANCELED//ОТМЕНЕНО СОЗДАТЕЛЕМ
}
