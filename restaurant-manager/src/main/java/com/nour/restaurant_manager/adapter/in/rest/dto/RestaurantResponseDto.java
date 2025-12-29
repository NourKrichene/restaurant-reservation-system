package com.nour.restaurant_manager.adapter.in.rest.dto;

import java.util.List;
import java.util.UUID;

public record RestaurantResponseDto(UUID id, String name, String description, List<WorkingTimeDto> workingTimes,
                                    List<ExceptionalClosingTimeDto> exceptionalClosingTimes, int capacity) {
}
