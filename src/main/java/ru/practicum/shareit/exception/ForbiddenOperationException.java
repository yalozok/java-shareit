package ru.practicum.shareit.exception;

public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(long userId, long itemId) {
        super(
                "Forbidden operation for user: " + userId + ", item: " + itemId
        );
    }
}
