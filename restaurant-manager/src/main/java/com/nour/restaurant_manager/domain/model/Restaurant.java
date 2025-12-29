package com.nour.restaurant_manager.domain.model;

import java.util.List;
import java.util.UUID;

public record Restaurant(UUID id, String name, String description, List<WorkingTime> workingTimes,
                         List<ExceptionalClosingTime> exceptionalClosingTimes, int capacity) {
    public Restaurant {
        if (name == null || name.isBlank() || workingTimes == null || capacity < 1) {
            throw new IllegalStateException("Unable to initialize Restaurant (" + "id=" + id + ", name=" + name + ", description=" + description + ", workingTimes=" + workingTimes + ", exceptionalClosingTimes=" + exceptionalClosingTimes + ", capacity=" + capacity + ")");
        }
        workingTimes.forEach(wt -> {
            if (wt == null) throw new IllegalStateException("WorkingTime cannot be null");
        });
    }
}
