package com.nour.reservation_manager.application.port.in;

import com.nour.reservation_manager.domain.model.Reservation;

import java.util.Optional;
import java.util.UUID;

public interface GetReservationUseCase {
    Optional<Reservation> getReservationById(UUID id);
}