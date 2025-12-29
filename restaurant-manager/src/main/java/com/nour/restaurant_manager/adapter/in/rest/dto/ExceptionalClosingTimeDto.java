package com.nour.restaurant_manager.adapter.in.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ExceptionalClosingTimeDto(Long id,
                                        @NotNull(message = "startTime must not be null") LocalDateTime startTime,
                                        @NotNull(message = "endTime must not be null") LocalDateTime endTime,
                                        String reason) {
}
