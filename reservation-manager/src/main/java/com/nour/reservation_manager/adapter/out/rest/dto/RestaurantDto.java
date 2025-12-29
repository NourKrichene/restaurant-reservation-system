package com.nour.reservation_manager.adapter.out.rest.dto;

import java.util.List;
import java.util.UUID;

public record RestaurantDto(UUID id, List<WorkingTimeDto> workingTimes, List<ExceptionalClosingTimeDto> exceptionalClosingTimes, int capacity) {
}
