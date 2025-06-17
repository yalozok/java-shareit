package ru.practicum.shareit.exception;

public class NotFoundRequestException extends RuntimeException {
    public NotFoundRequestException(long requestId) {
        super("Request with id " + requestId + " not found");
    }
}
