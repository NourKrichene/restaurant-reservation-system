package com.nour.restaurant_manager.application.port.in;

import com.nour.restaurant_manager.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface GetRestaurantUseCase {
    Page<Restaurant> getAllRestaurants(Pageable pageable);

    Optional<Restaurant> getRestaurantById(UUID id);
}
