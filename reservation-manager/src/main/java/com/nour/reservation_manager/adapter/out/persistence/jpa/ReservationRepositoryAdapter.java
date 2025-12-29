package com.nour.reservation_manager.adapter.out.persistence.jpa;

import com.nour.reservation_manager.application.port.out.ReservationRepository;
import com.nour.reservation_manager.domain.model.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReservationRepositoryAdapter implements ReservationRepository {

    private final SpringDataReservationRepository jpaRepository;

    public ReservationRepositoryAdapter(SpringDataReservationRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Reservation> findByCustomerEmailOrCustomerPhoneNumber(String customerEmail,
            String customerPhoneNumber) {
        return jpaRepository.findByCustomerEmailOrCustomerPhoneNumber(customerEmail, customerPhoneNumber).stream()
                .map(ReservationJpaMapper::toDomain).toList();
    }

    @Override
    public List<Reservation> findByRestaurantId(UUID restaurantId) {
        return jpaRepository.findByRestaurantId(restaurantId).stream()
                .map(ReservationJpaMapper::toDomain).toList();
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return jpaRepository.findById(id).map(ReservationJpaMapper::toDomain);
    }

    @Override
    public Reservation save(Reservation reservation) {
        var entity = ReservationJpaMapper.toEntity(reservation);
        var saved = jpaRepository.save(entity);
        return ReservationJpaMapper.toDomain(saved);
    }

    @Override
    public boolean delete(UUID id) {
        if (!jpaRepository.existsById(id))
            return false;
        jpaRepository.deleteById(id);
        return true;
    }
}
