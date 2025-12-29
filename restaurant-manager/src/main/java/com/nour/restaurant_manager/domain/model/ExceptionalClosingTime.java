package com.nour.restaurant_manager.domain.model;

import java.time.LocalDateTime;

public record ExceptionalClosingTime(Long id, LocalDateTime startTime, LocalDateTime endTime, String reason) {
    public ExceptionalClosingTime {
        if (startTime == null || endTime == null) {
            throw new IllegalStateException("Unable to initialize ExceptionalClosingTime (" + "id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", reason=" + reason + ")");
        }
        if (endTime.isBefore(startTime)) {
            throw new IllegalStateException("endTime must not be before startTime (" + "startTime=" + startTime + ", endTime=" + endTime + ")");
        }
    }
}
