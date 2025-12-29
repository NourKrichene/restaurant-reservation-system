package com.nour.reservation_manager.adapter.out.persistence.jpa;

import com.nour.reservation_manager.domain.model.Reservation;
import com.nour.reservation_manager.domain.model.ReservationDetails;

public class ReservationJpaMapper {

    private ReservationJpaMapper() {
    }

    public static Reservation toDomain(ReservationJpaEntity reservationJpaEntity) {
        return new Reservation(reservationJpaEntity.getId(), reservationJpaEntity.getCustomerEmail(),
                reservationJpaEntity.getCustomerPhoneNumber(), new ReservationDetails( reservationJpaEntity.getStartTime(), reservationJpaEntity.getEndTime(),
                reservationJpaEntity.getNumberOfGuests()),
                reservationJpaEntity.getStatus(), reservationJpaEntity.getRestaurantId());
    }

    public static ReservationJpaEntity toEntity(Reservation reservation) {
        ReservationJpaEntity jpaEntity = new ReservationJpaEntity();
        if (reservation.id() != null) {
            jpaEntity.setId(reservation.id());
        }
        jpaEntity.setCustomerEmail(reservation.customerEmail());
        jpaEntity.setCustomerPhoneNumber(reservation.customerPhoneNumber());
        jpaEntity.setStartTime(reservation.reservationDetails().startTime());
        jpaEntity.setEndTime(reservation.reservationDetails().endTime());
        jpaEntity.setNumberOfGuests(reservation.reservationDetails().numberOfGuests());
        jpaEntity.setStatus(reservation.status());
        jpaEntity.setRestaurantId(reservation.restaurantId());
        return jpaEntity;
    }
}
