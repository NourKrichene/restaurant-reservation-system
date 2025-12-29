package com.nour.reservation_manager.usecase;

import com.nour.reservation_manager.application.port.in.GetReservationUseCase;
import com.nour.reservation_manager.application.usecase.GetReservationUseCaseImpl;
import com.nour.reservation_manager.domain.model.Reservation;
import com.nour.reservation_manager.domain.model.ReservationDetails;
import com.nour.reservation_manager.domain.model.Status;
import com.nour.reservation_manager.usecase.fake.InMemoryReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetReservationUseCaseTest {

    private InMemoryReservationRepository repository;
    private GetReservationUseCase getReservationUseCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReservationRepository();
        getReservationUseCase = new GetReservationUseCaseImpl(repository);
    }

    @Test
    void shouldReturnReservationWhenItExists() {
        UUID id = UUID.randomUUID();
        Reservation reservation = new Reservation(id, "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID());
        repository.save(reservation);

        var result = getReservationUseCase.getReservationById(id);

        assertThat(result).isPresent().contains(reservation);
    }

    @Test
    void shouldReturnEmptyWhenReservationDoesNotExist() {
        UUID id = UUID.randomUUID();

        var result = getReservationUseCase.getReservationById(id);

        assertThat(result).isEmpty();
    }
}
