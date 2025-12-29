package com.nour.restaurant_manager.application.port.in;

import com.nour.restaurant_manager.domain.model.Restaurant;

import java.util.UUID;

public interface EditRestaurantUseCase {
    Restaurant updateRestaurant(UUID id, Restaurant restaurant);
}
