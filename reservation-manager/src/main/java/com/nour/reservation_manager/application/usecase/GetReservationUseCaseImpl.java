package com.nour.reservation_manager.application.usecase;

import com.nour.reservation_manager.application.port.in.GetReservationUseCase;
import com.nour.reservation_manager.application.port.out.ReservationRepository;
import com.nour.reservation_manager.domain.model.Reservation;

import java.util.Optional;
import java.util.UUID;

public class GetReservationUseCaseImpl implements GetReservationUseCase {
    private final ReservationRepository reservationRepository;

    public GetReservationUseCaseImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Optional<Reservation> getReservationById(UUID id) {
        return reservationRepository.findById(id);
    }
}
