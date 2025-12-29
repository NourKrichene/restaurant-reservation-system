package com.nour.restaurant_manager.adapter.in.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkingTimeDto(Long id, @NotNull(message = "dayOfWeek must not be null") DayOfWeek dayOfWeek,
                             @NotNull(message = "openTime must not be null") LocalTime openTime,
                             @NotNull(message = "closeTime must not be null") LocalTime closeTime) {

}
