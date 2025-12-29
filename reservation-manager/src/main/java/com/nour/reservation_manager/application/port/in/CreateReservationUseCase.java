package com.nour.reservation_manager.application.port.in;

import com.nour.reservation_manager.domain.model.Reservation;

public interface CreateReservationUseCase {
    Reservation addReservation(Reservation reservation);
}
