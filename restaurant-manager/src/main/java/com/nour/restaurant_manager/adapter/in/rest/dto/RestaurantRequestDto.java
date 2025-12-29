package com.nour.restaurant_manager.adapter.in.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record RestaurantRequestDto(@NotBlank(message = "name must not be blank") String name,
                                   @NotBlank(message = "description must not be blank") String description,
                                   List<WorkingTimeDto> workingTimes,
                                   List<ExceptionalClosingTimeDto> exceptionalClosingTimes,
                                   @Min(value = 1, message = "capacity must be at least 1") int capacity) {
}
