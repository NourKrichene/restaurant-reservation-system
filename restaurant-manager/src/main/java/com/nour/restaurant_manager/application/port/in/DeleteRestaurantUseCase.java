package com.nour.restaurant_manager.application.port.in;

import java.util.UUID;

public interface DeleteRestaurantUseCase {
    boolean deleteRestaurant(UUID id);
}
