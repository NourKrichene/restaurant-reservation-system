package com.nour.reservation_manager.domain.service;

import com.nour.reservation_manager.application.port.out.AvailabilityService;
import com.nour.reservation_manager.application.port.out.ReservationRepository;
import com.nour.reservation_manager.application.port.out.RestaurantProvider;
import com.nour.reservation_manager.domain.model.AvailabilityStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class AvailabilityServiceImpl implements AvailabilityService {
    private final RestaurantProvider restaurantProvider;
    private final ReservationRepository reservationRepository;

    public AvailabilityServiceImpl(RestaurantProvider restaurantProvider, ReservationRepository reservationRepository) {
        this.restaurantProvider = restaurantProvider;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public AvailabilityStatus isAvailableForReservation(UUID restaurantId, LocalDateTime startTime, LocalDateTime endTime, int numberOfGuests) {
        var restaurant = restaurantProvider.getRestaurantById(restaurantId);

        var isInWorkingTime = restaurant.workingTimes().stream().anyMatch(workingTime -> workingTime.isInWorkingTime(startTime, endTime));
        var isInExceptionalClosingTime = restaurant.exceptionalClosingTimes().stream().anyMatch(exceptionalClosingTime -> exceptionalClosingTime.isInExceptionalClosingTime(startTime, endTime));
        if (!isInWorkingTime || isInExceptionalClosingTime) {
            return AvailabilityStatus.TIME_SLOT_UNAVAILABLE;
        }

        var reservations = reservationRepository.findByRestaurantId(restaurantId);

        var interferingReservations = reservations.stream().filter(reservation -> reservation.reservationDetails().startTime().isBefore(endTime) && reservation.reservationDetails().endTime().isAfter(startTime)).toList();
        var totalGuests = interferingReservations.stream().mapToInt(reservation -> reservation.reservationDetails().numberOfGuests()).sum();

        if ((restaurant.capacity() - totalGuests) >= numberOfGuests) {
            return AvailabilityStatus.AVAILABLE;
        } else {
            return AvailabilityStatus.CAPACITY_EXCEEDED;
        }
    }

}
