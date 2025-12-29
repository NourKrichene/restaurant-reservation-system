package com.nour.reservation_manager.adapter.in.rest.dto;

import com.nour.reservation_manager.domain.model.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReservationRequestDto(@NotBlank(message = "customerEmail must not be blank") String customerEmail,
                                    @NotBlank(message = "customerPhoneNumber must not be blank") String customerPhoneNumber,
                                    @NotNull(message = "reservationDetails must not be null") @Valid ReservationDetailsDto reservationDetails,
                                    Status status,
                                    @NotNull(message = "restaurantId must not be null") UUID restaurantId) {
}
