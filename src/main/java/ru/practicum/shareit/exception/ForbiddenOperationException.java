package ru.practicum.shareit.exception;

public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(long userId) {
        super(
                "Forbidden operation for this user: " + userId
        );
    }
}
