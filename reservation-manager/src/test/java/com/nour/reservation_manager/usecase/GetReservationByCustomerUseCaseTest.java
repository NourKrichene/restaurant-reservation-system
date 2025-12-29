package com.nour.reservation_manager.usecase;

import com.nour.reservation_manager.application.port.in.GetReservationByCustomerUseCase;
import com.nour.reservation_manager.application.usecase.GetReservationByCustomerUseCaseImpl;
import com.nour.reservation_manager.domain.model.Reservation;
import com.nour.reservation_manager.domain.model.ReservationDetails;
import com.nour.reservation_manager.domain.model.Status;
import com.nour.reservation_manager.usecase.fake.InMemoryReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetReservationByCustomerUseCaseTest {

    private InMemoryReservationRepository repository;
    private GetReservationByCustomerUseCase getReservationByCustomerUseCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReservationRepository();
        getReservationByCustomerUseCase = new GetReservationByCustomerUseCaseImpl(repository);
    }

    @Test
    void shouldReturnClientReservationsByCustomerEmailOrCustomerPhoneNumber() {
        repository.save(new Reservation(UUID.randomUUID(), "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID()));
        repository.save(new Reservation(UUID.randomUUID(), "customer1new@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 7), Status.CREATED, UUID.randomUUID()));
        repository.save(new Reservation(UUID.randomUUID(), "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 6), Status.CREATED, UUID.randomUUID()));
        repository.save(new Reservation(UUID.randomUUID(), "customer3@mail.com", "0687654349", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 2), Status.CREATED, UUID.randomUUID()));

        List<Reservation> result = getReservationByCustomerUseCase.findByCustomerEmailOrCustomerPhoneNumber(null, "0612345678");

        assertThat(result).hasSize(3);
        assertThat(result).extracting(Reservation::id).doesNotContainNull();
        assertThat(result).extracting(Reservation::customerEmail).containsExactlyInAnyOrder("customer1@mail.com", "customer1new@mail.com", "customer1@mail.com");
        assertThat(result).extracting(Reservation::customerPhoneNumber).containsExactlyInAnyOrder("0612345678", "0612345678", "0612345678");
        assertThat(result).allMatch(reservation -> reservation.status() == Status.CREATED);
        assertThat(result).extracting(Reservation::reservationDetails).extracting(ReservationDetails::numberOfGuests).containsExactlyInAnyOrder(4, 7, 6);
        assertThat(result).extracting(Reservation::reservationDetails).extracting(ReservationDetails::startTime).allMatch(startTime -> startTime.equals(LocalDateTime.parse("2025-12-29T13:00:00")));
        assertThat(result).extracting(Reservation::reservationDetails).extracting(ReservationDetails::endTime).allMatch(endTime -> endTime.equals(LocalDateTime.parse("2025-12-29T14:00:00")));

    }
}
