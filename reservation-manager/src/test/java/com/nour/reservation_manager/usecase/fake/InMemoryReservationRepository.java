package com.nour.reservation_manager.usecase.fake;

import com.nour.reservation_manager.application.port.out.ReservationRepository;
import com.nour.reservation_manager.domain.model.Reservation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryReservationRepository implements ReservationRepository {

    private final Map<UUID, Reservation> store = new ConcurrentHashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        store.put(reservation.id(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Reservation> findByCustomerEmailOrCustomerPhoneNumber(String customerEmail,
            String customerPhoneNumber) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation : store.values()) {
            boolean emailMatches = customerEmail != null && customerEmail.equals(reservation.customerEmail());
            boolean phoneMatches = customerPhoneNumber != null
                    && customerPhoneNumber.equals(reservation.customerPhoneNumber());
            if (emailMatches || phoneMatches) {
                result.add(reservation);
            }
        }
        return result;
    }

    @Override
    public List<Reservation> findByRestaurantId(UUID restaurantId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation : store.values()) {
            if (restaurantId.equals(reservation.restaurantId())) {
                result.add(reservation);
            }
        }
        return result;
    }

    @Override
    public boolean delete(UUID id) {
        return store.remove(id) != null;
    }

    public LinkedList<Reservation> findAll() {
        return new LinkedList<>(store.values());
    }
}
