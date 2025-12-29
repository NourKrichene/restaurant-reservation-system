package com.nour.reservation_manager.domain.model;

import java.time.LocalDateTime;

public record ExceptionalClosingTime(Long id, LocalDateTime startTime, LocalDateTime endTime, String reason) {
    public boolean isInExceptionalClosingTime(LocalDateTime startTime, LocalDateTime endTime) {
        return (startTime.isBefore(this.endTime) && endTime.isAfter(this.startTime));
    }
}
