package ru.practicum.shareit.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(long userId) {
        super("User not found: " + userId);
    }
}
