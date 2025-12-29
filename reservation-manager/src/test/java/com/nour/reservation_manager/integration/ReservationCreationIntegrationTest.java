package com.nour.reservation_manager.integration;

import com.nour.reservation_manager.adapter.in.rest.dto.ReservationDetailsDto;
import com.nour.reservation_manager.adapter.in.rest.dto.ReservationRequestDto;
import com.nour.reservation_manager.adapter.in.rest.dto.ReservationResponseDto;
import com.nour.reservation_manager.application.port.out.ReservationRepository;
import com.nour.reservation_manager.application.port.out.RestaurantProvider;
import com.nour.reservation_manager.domain.model.ExceptionalClosingTime;
import com.nour.reservation_manager.domain.model.Restaurant;
import com.nour.reservation_manager.domain.model.Status;
import com.nour.reservation_manager.domain.model.WorkingTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.time.DayOfWeek.MONDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationCreationIntegrationTest {

    private static final String BASE_URL = "/api/v1/reservations";

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17").withDatabaseName("test").withUsername("test").withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReservationRepository repository;

    @MockitoBean
    private RestaurantProvider restaurantProvider;

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
    void shouldCreateReservation() {
        UUID restaurantId = UUID.fromString("00000000-0000-0000-0001-000000000001");
        ReservationRequestDto request = new ReservationRequestDto("customer@mail.com", "0123456789", new ReservationDetailsDto(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), null, restaurantId);

        var workingTimes = List.of(new WorkingTime(1L, MONDAY, LocalTime.parse("12:00:00"), LocalTime.parse("14:30:00")), new WorkingTime(2L, MONDAY, LocalTime.parse("19:00:00"), LocalTime.parse("22:30:00")));

        when(restaurantProvider.getRestaurantById(restaurantId)).thenReturn(new Restaurant(restaurantId, workingTimes, Collections.emptyList(), 50));
        ResponseEntity<ReservationResponseDto> createResponse = restTemplate.postForEntity(BASE_URL, request, ReservationResponseDto.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(createResponse.getBody());
        UUID id = createResponse.getBody().id();
        assertThat(id).isNotNull();
        assertThat(repository.findById(id)).isPresent();
        assertThat(createResponse.getBody().customerEmail()).isEqualTo("customer@mail.com");
        assertThat(createResponse.getBody().customerPhoneNumber()).isEqualTo("0123456789");
        assertThat(createResponse.getBody().status()).isEqualTo(Status.CREATED);
        assertThat(createResponse.getBody().reservationDetails().numberOfGuests()).isEqualTo(4);
        assertThat(createResponse.getBody().reservationDetails().startTime()).isEqualTo(LocalDateTime.parse("2025-12-29T13:00:00"));
        assertThat(createResponse.getBody().reservationDetails().endTime()).isEqualTo(LocalDateTime.parse("2025-12-29T14:00:00"));
    }

    @Test
    void shouldFailWhenCapacityExceeded() {
        UUID restaurantId = UUID.fromString("00000000-0000-0000-0001-000000000001");

        var workingTimes = List.of(new WorkingTime(1L, MONDAY, LocalTime.parse("12:00:00"), LocalTime.parse("14:30:00")), new WorkingTime(2L, MONDAY, LocalTime.parse("19:00:00"), LocalTime.parse("22:30:00")));
        when(restaurantProvider.getRestaurantById(restaurantId)).thenReturn(new Restaurant(restaurantId, workingTimes, Collections.emptyList(), 10));

        ReservationRequestDto request = new ReservationRequestDto("first@mail.com", "0123456789", new ReservationDetailsDto(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 11), null, restaurantId);
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("CAPACITY_EXCEEDED");
    }

    @Test
    void shouldFailWhenTimeSlotNotAvailable() {
        UUID restaurantId = UUID.fromString("00000000-0000-0000-0001-000000000001");

        var workingTimes = List.of(new WorkingTime(1L, MONDAY, LocalTime.parse("12:00:00"), LocalTime.parse("14:30:00")), new WorkingTime(2L, MONDAY, LocalTime.parse("19:00:00"), LocalTime.parse("22:30:00")));
        when(restaurantProvider.getRestaurantById(restaurantId)).thenReturn(new Restaurant(restaurantId, workingTimes, Collections.emptyList(), 50));

        ReservationRequestDto request = new ReservationRequestDto("customer@mail.com", "0123456789", new ReservationDetailsDto(LocalDateTime.parse("2025-12-29T16:00:00"), LocalDateTime.parse("2025-12-29T17:00:00"), 4), null, restaurantId);
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("TIME_SLOT_UNAVAILABLE");
    }

    @Test
    void shouldFailWhenExceptionalClosingTime() {
        UUID restaurantId = UUID.fromString("00000000-0000-0000-0001-000000000001");

        var workingTimes = List.of(new WorkingTime(1L, MONDAY, LocalTime.parse("12:00:00"), LocalTime.parse("14:30:00")), new WorkingTime(2L, MONDAY, LocalTime.parse("19:00:00"), LocalTime.parse("22:30:00")));

        var exceptionalClosingTimes = List.of(new ExceptionalClosingTime(1L, LocalDateTime.parse("2025-12-29T12:00:00"), LocalDateTime.parse("2025-12-29T15:00:00"), "Private Event"));
        when(restaurantProvider.getRestaurantById(restaurantId)).thenReturn(new Restaurant(restaurantId, workingTimes, exceptionalClosingTimes, 50));

        ReservationRequestDto request = new ReservationRequestDto("customer@mail.com", "0123456789", new ReservationDetailsDto(LocalDateTime.parse("2025-12-29T13:00:00"), LocalDateTime.parse("2025-12-29T14:00:00"), 4), null, restaurantId);
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("TIME_SLOT_UNAVAILABLE");
    }
}