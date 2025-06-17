package ru.practicum.shareit.exception;

public class NotAvailableItemException extends RuntimeException {
    public NotAvailableItemException(long id) {
        super("Item with id " + id + " is not available");
    }
}
