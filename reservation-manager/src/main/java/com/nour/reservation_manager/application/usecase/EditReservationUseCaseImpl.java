package com.nour.reservation_manager.application.usecase;

import com.nour.reservation_manager.application.port.in.EditReservationUseCase;
import com.nour.reservation_manager.application.port.out.ReservationRepository;
import com.nour.reservation_manager.domain.exception.ReservationNotFoundException;
import com.nour.reservation_manager.domain.model.Reservation;

import java.util.UUID;

public class EditReservationUseCaseImpl implements EditReservationUseCase {
    private final ReservationRepository reservationRepository;

    public EditReservationUseCaseImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation updateReservation(UUID id, Reservation reservation) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        Reservation reservationToUpdate = new Reservation(existingReservation.id(), reservation.customerEmail(),
                reservation.customerPhoneNumber(), reservation.reservationDetails(), reservation.status(),
                reservation.restaurantId());

        return reservationRepository.save(reservationToUpdate);
    }
}
