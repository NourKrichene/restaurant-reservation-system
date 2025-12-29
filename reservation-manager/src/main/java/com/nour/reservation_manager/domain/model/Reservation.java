package com.nour.reservation_manager.domain.model;

import java.util.UUID;

public record Reservation(UUID id, String customerEmail, String customerPhoneNumber,
                          ReservationDetails reservationDetails, Status status, UUID restaurantId) {
    public Reservation {
        if (customerEmail == null || customerEmail.isBlank() || customerPhoneNumber == null || customerPhoneNumber.isBlank() || reservationDetails == null || restaurantId == null) {
            throw new IllegalStateException("Unable to initialize Reservation (" + "id=" + id + ", customerEmail=" + customerEmail + ", customerPhoneNumber=" + customerPhoneNumber + ", reservationDetails=" + reservationDetails + ", status=" + status + ", restaurantId=" + restaurantId + ")");
        }
    }
}
