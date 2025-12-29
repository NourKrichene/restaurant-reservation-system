package com.nour.reservation_manager.application.port.out;

import com.nour.reservation_manager.domain.model.Restaurant;

import java.util.UUID;

public interface RestaurantProvider {
    Restaurant getRestaurantById(UUID restaurantId);
}
