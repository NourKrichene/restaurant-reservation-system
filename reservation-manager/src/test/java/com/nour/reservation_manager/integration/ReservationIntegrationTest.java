package com.nour.reservation_manager.integration;

import com.nour.reservation_manager.adapter.in.rest.dto.ReservationDetailsDto;
import com.nour.reservation_manager.adapter.in.rest.dto.ReservationRequestDto;
import com.nour.reservation_manager.adapter.in.rest.dto.ReservationResponseDto;
import com.nour.reservation_manager.application.port.out.ReservationRepository;
import com.nour.reservation_manager.domain.model.Reservation;
import com.nour.reservation_manager.domain.model.ReservationDetails;
import com.nour.reservation_manager.domain.model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationIntegrationTest {

    private static final String BASE_URL = "/api/v1/reservations";

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17").withDatabaseName("test").withUsername("test").withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReservationRepository repository;

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void cleanup() {
        repository.delete(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        repository.delete(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        repository.delete(UUID.fromString("00000000-0000-0000-0000-000000000003"));
    }

    @Test
    void shouldRetrieveReservation() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        repository.save(new Reservation(id, "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID()));

        ResponseEntity<ReservationResponseDto> getResponse = restTemplate.getForEntity(BASE_URL + "/" + id, ReservationResponseDto.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(getResponse.getBody());
        assertThat(getResponse.getBody().customerEmail()).isEqualTo("customer1@mail.com");
        assertThat(getResponse.getBody().customerPhoneNumber()).isEqualTo("0612345678");
        assertThat(getResponse.getBody().status()).isEqualTo(Status.CREATED);
        assertThat(getResponse.getBody().reservationDetails().numberOfGuests()).isEqualTo(4);
        assertThat(getResponse.getBody().reservationDetails().startTime()).isEqualTo(LocalDateTime.parse("2025-12-29T13:00:00"));
        assertThat(getResponse.getBody().reservationDetails().endTime()).isEqualTo(LocalDateTime.parse("2025-12-29T14:00:00"));
    }

    @Test
    void shouldReturnNotFoundWhenRetrieveReservationNotExist() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        ResponseEntity<ReservationResponseDto> getResponse = restTemplate.getForEntity(BASE_URL + "/" + id, ReservationResponseDto.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"?email=customer1@mail.com", "?phone=0612345678", "?email=customer1@mail.com&phone=0612345678"})
    void shouldReturnClientReservations(String searchParams) {
        repository.save(new Reservation(UUID.fromString("00000000-0000-0000-0000-000000000001"), "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID()));
        repository.save(new Reservation(UUID.fromString("00000000-0000-0000-0000-000000000002"), "customer2@mail.com", "0687654321", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 7), Status.CREATED, UUID.randomUUID()));
        repository.save(new Reservation(UUID.fromString("00000000-0000-0000-0000-000000000003"), "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 2), Status.CONFIRMED, UUID.randomUUID()));

        ResponseEntity<ReservationResponseDto[]> response = restTemplate.getForEntity(BASE_URL + searchParams, ReservationResponseDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void shouldUpdateReservation() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");
        repository.save(new Reservation(id, "customer1@mail.com", "0612345678", new ReservationDetails(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID()));

        ReservationRequestDto update = new ReservationRequestDto("customer1new@mail.com", "0612345600", new ReservationDetailsDto(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), Status.CREATED, UUID.randomUUID());

        restTemplate.put(BASE_URL + "/" + id, update);

        ResponseEntity<ReservationResponseDto> response = restTemplate.getForEntity(BASE_URL + "/" + id, ReservationResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().customerEmail()).isEqualTo("customer1new@mail.com");
        assertThat(response.getBody().customerPhoneNumber()).isEqualTo("0612345600");
        assertThat(response.getBody().status()).isEqualTo(Status.CREATED);
        assertThat(response.getBody().reservationDetails().numberOfGuests()).isEqualTo(4);
        assertThat(response.getBody().reservationDetails().startTime()).isEqualTo(LocalDateTime.parse("2025-12-29T13:00:00"));
        assertThat(response.getBody().reservationDetails().endTime()).isEqualTo(LocalDateTime.parse("2025-12-29T14:00:00"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdateReservationNotExist() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        ReservationRequestDto update = new ReservationRequestDto("customer3@mail.com", "0612345670", new ReservationDetailsDto(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 2), Status.CREATED, UUID.randomUUID());

        ResponseEntity<Void> response = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.PUT, new HttpEntity<>(update), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}