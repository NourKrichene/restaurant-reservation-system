package com.nour.reservation_manager.application.port.in;

import com.nour.reservation_manager.domain.model.Reservation;

import java.util.List;

public interface GetReservationByCustomerUseCase {
    List<Reservation> findByCustomerEmailOrCustomerPhoneNumber(String customerEmail, String customerPhoneNumber);
}