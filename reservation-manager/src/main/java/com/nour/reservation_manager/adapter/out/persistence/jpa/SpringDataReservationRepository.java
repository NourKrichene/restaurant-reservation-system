package com.nour.reservation_manager.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataReservationRepository extends JpaRepository<ReservationJpaEntity, UUID> {
    List<ReservationJpaEntity> findByCustomerEmailOrCustomerPhoneNumber(String customerEmail,
            String customerPhoneNumber);
    List<ReservationJpaEntity> findByRestaurantId(UUID restaurantId);
}
