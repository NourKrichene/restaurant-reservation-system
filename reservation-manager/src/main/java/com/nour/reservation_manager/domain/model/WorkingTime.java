package com.nour.reservation_manager.domain.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record WorkingTime(Long id, DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime) {
    public boolean isInWorkingTime(LocalDateTime startTime, LocalDateTime endTime) {
        DayOfWeek reservationDay = startTime.getDayOfWeek();
        LocalTime reservationStartTime = startTime.toLocalTime();
        LocalTime reservationEndTime = endTime.toLocalTime();

        return this.dayOfWeek.equals(reservationDay) &&
               !reservationStartTime.isBefore(this.openTime) &&
               !reservationEndTime.isAfter(this.closeTime);
    }
}
