package com.nour.reservation_manager.adapter.in.rest.mapper;

import com.nour.reservation_manager.adapter.in.rest.dto.ReservationDetailsDto;
import com.nour.reservation_manager.adapter.in.rest.dto.ReservationRequestDto;
import com.nour.reservation_manager.adapter.in.rest.dto.ReservationResponseDto;
import com.nour.reservation_manager.domain.model.Reservation;
import com.nour.reservation_manager.domain.model.ReservationDetails;

public class ReservationRestMapper {

    private ReservationRestMapper() {
    }

    public static Reservation toDomain(ReservationRequestDto reservationRequest) {
        return new Reservation(null, reservationRequest.customerEmail(), reservationRequest.customerPhoneNumber(), new ReservationDetails(reservationRequest.reservationDetails().startTime(), reservationRequest.reservationDetails().endTime(), reservationRequest.reservationDetails().numberOfGuests()), reservationRequest.status(), reservationRequest.restaurantId());

    }

    public static ReservationResponseDto toResponse(Reservation reservation) {
        return new ReservationResponseDto(reservation.id(), reservation.customerEmail(), reservation.customerPhoneNumber(), new ReservationDetailsDto(reservation.reservationDetails().startTime(), reservation.reservationDetails().endTime(), reservation.reservationDetails().numberOfGuests()), reservation.status(), reservation.restaurantId());
    }
}
