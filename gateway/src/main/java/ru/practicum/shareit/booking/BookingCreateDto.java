package ru.practicum.shareit.booking;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingCreateDto {
    @NotNull
    @PositiveOrZero
    private long itemId;

    @NotNull
    @FutureOrPresent(message = "Start of booking must be in the present or future.")
    private LocalDateTime start;

    @NotNull
    @Future(message = "End of booking must be in the future.")
    private LocalDateTime end;

    @AssertTrue(message = "Start time must be earlier than end time.")
    boolean isStartBeforeEnd() {
        if (start == null || end == null) {
            return true; // let @NotNull handle it
        }
        return start.isBefore(end);
    }
}
