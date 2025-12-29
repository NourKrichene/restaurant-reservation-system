package com.nour.restaurant_manager.application.port.in;

import com.nour.restaurant_manager.domain.model.Restaurant;

public interface CreateRestaurantUseCase {
    Restaurant addRestaurant(Restaurant restaurant);
}
