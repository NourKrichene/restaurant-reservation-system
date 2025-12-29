package com.nour.reservation_manager.usecase;

import com.nour.reservation_manager.application.port.in.EditReservationUseCase;
import com.nour.reservation_manager.application.usecase.EditReservationUseCaseImpl;
import com.nour.reservation_manager.domain.exception.ReservationNotFoundException;
import com.nour.reservation_manager.domain.model.Reservation;
import com.nour.reservation_manager.domain.model.ReservationDetails;
import com.nour.reservation_manager.domain.model.Status;
import com.nour.reservation_manager.usecase.fake.InMemoryReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EditReservationUseCaseTest {

    private InMemoryReservationRepository repository;
    private EditReservationUseCase editReservationUseCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReservationRepository();
        editReservationUseCase = new EditReservationUseCaseImpl(repository);
    }

    @Test
    void shouldUpdateExistingReservation() {
        UUID id = UUID.randomUUID();
        repository.save(new Reservation(id, "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID()));

        Reservation updatedReservation = new Reservation(null, "customer1new@mail.com", "0612345600", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID());

        Reservation result = editReservationUseCase.updateReservation(id, updatedReservation);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.customerEmail()).isEqualTo("customer1new@mail.com");
        assertThat(result.customerPhoneNumber()).isEqualTo("0612345600");
        assertThat(result.status()).isEqualTo(Status.CREATED);
        assertThat(result.reservationDetails().numberOfGuests()).isEqualTo(4);
        assertThat(result.reservationDetails().startTime()).isEqualTo(LocalDateTime.parse("2025-12-29T13:00:00"));
        assertThat(result.reservationDetails().endTime()).isEqualTo(LocalDateTime.parse("2025-12-29T14:00:00"));
    }

    @Test
    void shouldThrowExceptionWhenReservationDoesNotExist() {
        UUID id = UUID.randomUUID();
        Reservation updatedReservation = new Reservation(null, "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID());

        assertThatThrownBy(() -> editReservationUseCase.updateReservation(id, updatedReservation)).isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void shouldPreserveOriginalIdEvenIfUpdatedReservationContainsAnotherId() {
        UUID originalId = UUID.randomUUID();
        repository.save(new Reservation(originalId, "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID()));

        Reservation updatedReservation = new Reservation(UUID.randomUUID(), "customer1new@mail.com", "0612345600", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID());

        Reservation result = editReservationUseCase.updateReservation(originalId, updatedReservation);

        assertThat(result.id()).isEqualTo(originalId);
    }
}
