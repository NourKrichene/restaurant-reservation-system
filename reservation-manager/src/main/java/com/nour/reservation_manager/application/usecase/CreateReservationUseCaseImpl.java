package com.nour.reservation_manager.application.usecase;

import com.nour.reservation_manager.application.port.in.CreateReservationUseCase;
import com.nour.reservation_manager.application.port.out.AvailabilityService;
import com.nour.reservation_manager.application.port.out.IdGenerator;
import com.nour.reservation_manager.application.port.out.ReservationRepository;
import com.nour.reservation_manager.domain.exception.ReservationNotAvailableException;
import com.nour.reservation_manager.domain.model.AvailabilityStatus;
import com.nour.reservation_manager.domain.model.Reservation;
import com.nour.reservation_manager.domain.model.Status;

public class CreateReservationUseCaseImpl implements CreateReservationUseCase {
    private final ReservationRepository reservationRepository;
    private final IdGenerator idGenerator;
    private final AvailabilityService availabilityService;

    public CreateReservationUseCaseImpl(ReservationRepository reservationRepository, IdGenerator idGenerator, AvailabilityService availabilityService) {
        this.reservationRepository = reservationRepository;
        this.idGenerator = idGenerator;
        this.availabilityService = availabilityService;
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        AvailabilityStatus availabilityStatus = availabilityService.isAvailableForReservation(reservation.restaurantId(), reservation.reservationDetails().startTime(), reservation.reservationDetails().endTime(), reservation.reservationDetails().numberOfGuests());
        if (availabilityStatus != AvailabilityStatus.AVAILABLE) {
            throw new ReservationNotAvailableException("The reservation cannot be created due to availability issues: " + availabilityStatus);
        }
        Reservation reservationToAdd = new Reservation(idGenerator.generate(), reservation.customerEmail(), reservation.customerPhoneNumber(), reservation.reservationDetails(), Status.CREATED, reservation.restaurantId());
        return reservationRepository.save(reservationToAdd);
    }
}
