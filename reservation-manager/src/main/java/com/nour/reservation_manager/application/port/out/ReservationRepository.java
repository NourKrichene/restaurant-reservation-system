package com.nour.reservation_manager.application.port.out;

import com.nour.reservation_manager.domain.model.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    Optional<Reservation> findById(UUID id);

    Reservation save(Reservation reservation);

    boolean delete(UUID id);

    List<Reservation> findByCustomerEmailOrCustomerPhoneNumber(String customerEmail, String customerPhoneNumber);

    List<Reservation> findByRestaurantId(UUID restaurantId);
}
