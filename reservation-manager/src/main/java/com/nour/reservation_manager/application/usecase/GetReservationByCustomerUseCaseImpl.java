package com.nour.reservation_manager.application.usecase;

import com.nour.reservation_manager.application.port.in.GetReservationByCustomerUseCase;
import com.nour.reservation_manager.application.port.out.ReservationRepository;
import com.nour.reservation_manager.domain.model.Reservation;

import java.util.List;

public class GetReservationByCustomerUseCaseImpl implements GetReservationByCustomerUseCase {
    private final ReservationRepository reservationRepository;

    public GetReservationByCustomerUseCaseImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> findByCustomerEmailOrCustomerPhoneNumber(String customerEmail,
            String customerPhoneNumber) {
        return reservationRepository.findByCustomerEmailOrCustomerPhoneNumber(customerEmail, customerPhoneNumber);
    }

}
