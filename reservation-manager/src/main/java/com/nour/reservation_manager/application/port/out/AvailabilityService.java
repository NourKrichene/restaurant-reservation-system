package com.nour.reservation_manager.application.port.out;

import com.nour.reservation_manager.domain.model.AvailabilityStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AvailabilityService {
    AvailabilityStatus isAvailableForReservation(UUID restaurantId, LocalDateTime startTime, LocalDateTime endTime, int numberOfGuests);
}
