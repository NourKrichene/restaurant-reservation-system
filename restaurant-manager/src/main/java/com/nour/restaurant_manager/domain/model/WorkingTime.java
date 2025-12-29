package com.nour.restaurant_manager.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkingTime(Long id, DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime) {
    public WorkingTime {
        if (dayOfWeek == null || openTime == null || closeTime == null) {
            throw new IllegalStateException("Unable to initialize WorkingTime (" + "id=" + id + ", dayOfWeek=" + dayOfWeek + ", openTime=" + openTime + ", closeTime=" + closeTime + ")");
        }
        if (closeTime.isBefore(openTime) || closeTime.equals(openTime)) {
            throw new IllegalStateException("closeTime must be after openTime (" + "openTime=" + openTime + ", closeTime=" + closeTime + ")");
        }
    }
}
