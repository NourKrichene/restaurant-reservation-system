package com.nour.reservation_manager.application.port.in;

import com.nour.reservation_manager.domain.model.Reservation;

import java.util.UUID;

public interface EditReservationUseCase {
    Reservation updateReservation(UUID id, Reservation reservation);
}
