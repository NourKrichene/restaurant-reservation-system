package com.nour.restaurant_manager.usecase;


import com.nour.restaurant_manager.application.port.in.CreateRestaurantUseCase;
import com.nour.restaurant_manager.application.usecase.CreateRestaurantUseCaseImpl;
import com.nour.restaurant_manager.domain.model.Restaurant;
import com.nour.restaurant_manager.usecase.fake.InMemoryRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CreateRestaurantUseCaseTest {

    private InMemoryRestaurantRepository restaurantRepository;
    private CreateRestaurantUseCase createRestaurantUseCase;

    @BeforeEach
    void setUp() {
        restaurantRepository = new InMemoryRestaurantRepository();
        createRestaurantUseCase = new CreateRestaurantUseCaseImpl(restaurantRepository, UUID::randomUUID);
    }

    @Test
    void shouldCreateRestaurant() {
        Restaurant restaurant = new Restaurant(null, "La Storia Di Italia", "Authentic Italian Food", Collections.emptyList(), Collections.emptyList(), 50);

        createRestaurantUseCase.addRestaurant(restaurant);

        assertThat(restaurantRepository.findAll(Pageable.ofSize(1)).getTotalElements()).isEqualTo(1);
        Restaurant stored = restaurantRepository.findAll(Pageable.ofSize(1)).getContent().getFirst();

        assertThat(stored.id()).isNotNull();
        assertThat(stored.name()).isEqualTo("La Storia Di Italia");
        assertThat(stored.description()).isEqualTo("Authentic Italian Food");
    }
}
