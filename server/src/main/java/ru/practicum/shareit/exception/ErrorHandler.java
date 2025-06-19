package ru.practicum.shareit.exception;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Generated
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotAvailableItem(NotAvailableItemException e) {
        log.info("400 {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({NotFoundItemException.class,
            NotFoundUserException.class,
            NotFoundBookingException.class,
            NotFoundRequestException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundEntity(Exception e) {
        log.info("404 {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(ForbiddenOperationException e) {
        log.info("403 {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleForbidden(UserAlreadyExistException e) {
        log.info("409 {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, ValueInstantiationException.class,
            HttpMessageNotReadableException.class, MissingRequestHeaderException.class})
    public void handleIllegalArgumentException(Exception e) {
        throw new ValidationException(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(ValidationException e) {
        log.trace("Validation: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.warn("Error: ", e);
        return new ErrorResponse(e.getMessage());
    }
}
