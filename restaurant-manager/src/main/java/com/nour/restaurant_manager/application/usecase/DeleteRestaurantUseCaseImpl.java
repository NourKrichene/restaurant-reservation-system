package com.nour.restaurant_manager.application.usecase;

import com.nour.restaurant_manager.application.port.in.DeleteRestaurantUseCase;
import com.nour.restaurant_manager.application.port.out.RestaurantRepository;

import java.util.UUID;

public class DeleteRestaurantUseCaseImpl implements DeleteRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;

    public DeleteRestaurantUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public boolean deleteRestaurant(UUID id) {
        return restaurantRepository.delete(id);
    }
}
