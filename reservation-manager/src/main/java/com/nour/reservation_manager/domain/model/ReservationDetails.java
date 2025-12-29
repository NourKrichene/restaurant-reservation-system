package com.nour.reservation_manager.domain.model;

import java.time.LocalDateTime;

public record ReservationDetails(LocalDateTime startTime, LocalDateTime endTime, int numberOfGuests) {
    public ReservationDetails {
        if (startTime == null || endTime == null) {
            throw new IllegalStateException("Unable to initialize ReservationDetails (" + "startTime=" + startTime + ", endTime=" + endTime + ", numberOfGuests=" + numberOfGuests + ")");
        }
        if (endTime.isBefore(startTime)) {
            throw new IllegalStateException("endTime must not be before startTime (" + "startTime=" + startTime + ", endTime=" + endTime + ")");
        }
        if (numberOfGuests < 1) {
            throw new IllegalStateException("numberOfGuests must be at least 1 (" + "numberOfGuests=" + numberOfGuests + ")");
        }
    }
}
