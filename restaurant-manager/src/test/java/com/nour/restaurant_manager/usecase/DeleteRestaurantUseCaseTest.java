package com.nour.restaurant_manager.usecase;

import com.nour.restaurant_manager.application.port.in.DeleteRestaurantUseCase;
import com.nour.restaurant_manager.application.usecase.DeleteRestaurantUseCaseImpl;
import com.nour.restaurant_manager.domain.model.Restaurant;
import com.nour.restaurant_manager.usecase.fake.InMemoryRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteRestaurantUseCaseTest {

    private InMemoryRestaurantRepository repository;
    private DeleteRestaurantUseCase deleteRestaurantUseCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRestaurantRepository();
        deleteRestaurantUseCase = new DeleteRestaurantUseCaseImpl(repository);
    }

    @Test
    void shouldDeleteExistingRestaurant() {
        UUID id = UUID.randomUUID();
        repository.save(new Restaurant(id, "La Storia Di Italia", "Authentic Italian Food", Collections.emptyList(), Collections.emptyList(), 50));

        boolean deleted = deleteRestaurantUseCase.deleteRestaurant(id);

        assertThat(deleted).isTrue();
        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    void shouldReturnFalseWhenRestaurantDoesNotExist() {
        UUID id = UUID.randomUUID();

        boolean deleted = deleteRestaurantUseCase.deleteRestaurant(id);

        assertThat(deleted).isFalse();
        assertThat(repository.findAll(Pageable.ofSize(1)).getTotalElements()).isZero();
    }
}
