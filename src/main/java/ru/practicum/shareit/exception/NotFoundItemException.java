package ru.practicum.shareit.exception;

public class NotFoundItemException extends RuntimeException {
    public NotFoundItemException(long id) {
        super("Item not found: " + id);
    }
}
