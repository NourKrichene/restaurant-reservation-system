package com.nour.reservation_manager.usecase;

import com.nour.reservation_manager.application.port.in.CreateReservationUseCase;
import com.nour.reservation_manager.application.port.out.RestaurantProvider;
import com.nour.reservation_manager.application.usecase.CreateReservationUseCaseImpl;
import com.nour.reservation_manager.domain.exception.ReservationNotAvailableException;
import com.nour.reservation_manager.domain.model.*;
import com.nour.reservation_manager.domain.service.AvailabilityServiceImpl;
import com.nour.reservation_manager.usecase.fake.InMemoryReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.time.DayOfWeek.MONDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CreateReservationUseCaseTest {

    private InMemoryReservationRepository reservationRepository;
    private CreateReservationUseCase createReservationUseCase;

    @BeforeEach
    void setUp() {
        reservationRepository = new InMemoryReservationRepository();
        var workingTimes = List.of(new WorkingTime(1L, MONDAY, LocalTime.parse("12:00:00"), LocalTime.parse("14:30:00")), new WorkingTime(2L, MONDAY, LocalTime.parse("19:00:00"), LocalTime.parse("22:30:00")));

        RestaurantProvider restaurantProvider = restaurantId -> new Restaurant(restaurantId, workingTimes, Collections.emptyList(), 50);

        createReservationUseCase = new CreateReservationUseCaseImpl(reservationRepository, UUID::randomUUID, new AvailabilityServiceImpl(restaurantProvider, reservationRepository));
    }

    @Test
    void shouldCreateReservation() {
        Reservation reservation = new Reservation(null, "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), null, UUID.randomUUID());

        createReservationUseCase.addReservation(reservation);

        assertThat(reservationRepository.findAll()).hasSize(1);
        Reservation stored = reservationRepository.findAll().getFirst();

        assertThat(stored.id()).isNotNull();
        assertThat(stored.customerEmail()).isEqualTo("customer1@mail.com");
        assertThat(stored.customerPhoneNumber()).isEqualTo("0612345678");
        assertThat(stored.status()).isEqualTo(Status.CREATED);
        assertThat(stored.reservationDetails().numberOfGuests()).isEqualTo(4);
        assertThat(stored.reservationDetails().startTime()).isEqualTo(LocalDateTime.parse("2025-12-29T13:00:00"));
        assertThat(stored.reservationDetails().endTime()).isEqualTo(LocalDateTime.parse("2025-12-29T14:00:00"));
    }

    @Test
    void shouldThrowExceptionWhenCreateReservationThatExceedsCapacity() {
        Reservation reservation = new Reservation(null, "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 60), null, UUID.randomUUID());

        assertThatThrownBy(() -> createReservationUseCase.addReservation(reservation)).isInstanceOf(ReservationNotAvailableException.class).hasMessage("The reservation cannot be created due to availability issues: CAPACITY_EXCEEDED");

        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenCreateReservationNotInAvailableTime() {
        Reservation reservation = new Reservation(null, "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T16:00:00"), LocalDateTime.parse("2025-12-29T17:00:00"), 4), null, UUID.randomUUID());

        assertThatThrownBy(() -> createReservationUseCase.addReservation(reservation)).isInstanceOf(ReservationNotAvailableException.class).hasMessage("The reservation cannot be created due to availability issues: TIME_SLOT_UNAVAILABLE");

        assertThat(reservationRepository.findAll()).isEmpty();
    }
}
