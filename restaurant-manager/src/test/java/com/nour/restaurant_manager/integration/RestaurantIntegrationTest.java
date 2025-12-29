package com.nour.restaurant_manager.integration;

import com.nour.restaurant_manager.adapter.in.rest.dto.RestaurantRequestDto;
import com.nour.restaurant_manager.adapter.in.rest.dto.RestaurantResponseDto;
import com.nour.restaurant_manager.adapter.out.persistence.jpa.SpringDataRestaurantRepository;
import com.nour.restaurant_manager.adapter.out.persistence.jpa.entity.RestaurantJpaEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantIntegrationTest {

    private static final String BASE_URL = "/api/v1/restaurants";
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17").withDatabaseName("test").withUsername("test").withPassword("test");
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SpringDataRestaurantRepository repository;

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void shouldCreateRestaurant() {
        RestaurantRequestDto request = new RestaurantRequestDto("La Storia Di Italia", "Authentic Italian Food", Collections.emptyList(), Collections.emptyList(), 50);

        ResponseEntity<RestaurantResponseDto> createResponse = restTemplate.postForEntity(BASE_URL, request, RestaurantResponseDto.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(createResponse.getBody());
        UUID id = createResponse.getBody().id();
        assertThat(id).isNotNull();
        assertThat(repository.findById(id)).isPresent();
    }

    @Test
    void shouldRetrieveRestaurant() {
        UUID id = UUID.randomUUID();
        repository.save(new RestaurantJpaEntity(id, "La Storia Di Italia", "Authentic Italian Food", Collections.emptyList(), Collections.emptyList(), 50));

        ResponseEntity<RestaurantResponseDto> getResponse = restTemplate.getForEntity(BASE_URL + "/" + id, RestaurantResponseDto.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(getResponse.getBody());
        assertThat(getResponse.getBody().name()).isEqualTo("La Storia Di Italia");
        assertThat(getResponse.getBody().description()).isEqualTo("Authentic Italian Food");
    }

    @Test
    void shouldReturnNotFoundWhenRetrieveRestaurantNotExist() {
        UUID id = UUID.randomUUID();

        ResponseEntity<RestaurantResponseDto> getResponse = restTemplate.getForEntity(BASE_URL + "/" + id, RestaurantResponseDto.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldRetrieveAllRestaurants() {
        RestaurantJpaEntity restaurant1 = new RestaurantJpaEntity(UUID.randomUUID(), "La Storia Di Italia", "Authentic Italian Food", Collections.emptyList(), Collections.emptyList(), 50);

        RestaurantJpaEntity restaurant2 = new RestaurantJpaEntity(UUID.randomUUID(), "Sushi Saito", "Fresh sea food", Collections.emptyList(), Collections.emptyList(), 30);

        repository.saveAll(List.of(restaurant1, restaurant2));

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(BASE_URL + "?page=0&size=10", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertNotNull(response.getBody());
        @SuppressWarnings("unchecked") List<Map<String, Object>> content = (List<Map<String, Object>>) response.getBody().get("content");

        assertThat(content).hasSize(2);
        assertThat(content).extracting(e -> e.get("name")).containsExactlyInAnyOrder("La Storia Di Italia", "Sushi Saito");

        assertThat(content).extracting(e -> e.get("description")).containsExactlyInAnyOrder("Authentic Italian Food", "Fresh sea food");
    }


    @Test
    void shouldUpdateRestaurant() {
        UUID id = UUID.randomUUID();
        repository.save(new RestaurantJpaEntity(id, "La Storia Di Italia", "Authentic Italian Food", Collections.emptyList(), Collections.emptyList(), 50));

        RestaurantRequestDto update = new RestaurantRequestDto("La Storia", "Authentic Tasty Italian Food", Collections.emptyList(), Collections.emptyList(), 50);
        restTemplate.put(BASE_URL + "/" + id, update);

        ResponseEntity<RestaurantResponseDto> response = restTemplate.getForEntity(BASE_URL + "/" + id, RestaurantResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().name()).isEqualTo("La Storia");
        assertThat(response.getBody().description()).isEqualTo("Authentic Tasty Italian Food");
    }

    @Test
    void shouldReturnNotFoundWhenUpdateRestaurantNotExist() {
        UUID id = UUID.randomUUID();
        RestaurantRequestDto update = new RestaurantRequestDto("Non existent", "No description", Collections.emptyList(), Collections.emptyList(), 1);

        ResponseEntity<Void> response = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.PUT, new HttpEntity<>(update), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDeleteRestaurant() {
        UUID id = UUID.randomUUID();
        repository.save(new RestaurantJpaEntity(id, "La Storia Di Italia", "Authentic Italian Food", Collections.emptyList(), Collections.emptyList(), 50));

        restTemplate.delete(BASE_URL + "/" + id);

        ResponseEntity<RestaurantResponseDto> response = restTemplate.getForEntity(BASE_URL + "/" + id, RestaurantResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    void shouldReturnNotFoundWhenDeleteRestaurantNotExist() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(repository.findById(id)).isEmpty();
    }

}
