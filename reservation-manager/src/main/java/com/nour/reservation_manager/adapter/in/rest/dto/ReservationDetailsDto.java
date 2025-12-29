package com.nour.reservation_manager.adapter.in.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ReservationDetailsDto(
        @NotNull(message = "startTime must not be null") LocalDateTime startTime,
        @NotNull(message = "endTime must not be null") LocalDateTime endTime,
        @Min(value = 1, message = "numberOfGuests must be at least 1") int numberOfGuests
) {}
