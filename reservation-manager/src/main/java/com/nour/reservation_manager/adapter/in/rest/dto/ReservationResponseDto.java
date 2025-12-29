package com.nour.reservation_manager.adapter.in.rest.dto;

import com.nour.reservation_manager.domain.model.Status;

import java.util.UUID;

public record ReservationResponseDto(UUID id, String customerEmail, String customerPhoneNumber, ReservationDetailsDto reservationDetails,
                Status status, UUID restaurantId) {
}
