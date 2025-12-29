package com.nour.reservation_manager.domain.model;

import java.util.List;
import java.util.UUID;

public record Restaurant(UUID id,  List<WorkingTime> workingTimes, List<ExceptionalClosingTime> exceptionalClosingTimes, int capacity) {
}
