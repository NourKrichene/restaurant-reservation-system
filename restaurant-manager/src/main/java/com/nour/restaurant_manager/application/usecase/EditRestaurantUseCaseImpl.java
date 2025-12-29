package com.nour.restaurant_manager.application.usecase;

import com.nour.restaurant_manager.application.port.in.EditRestaurantUseCase;
import com.nour.restaurant_manager.application.port.out.RestaurantRepository;
import com.nour.restaurant_manager.domain.exception.RestaurantNotFoundException;
import com.nour.restaurant_manager.domain.model.Restaurant;

import java.util.UUID;

public class EditRestaurantUseCaseImpl implements EditRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;

    public EditRestaurantUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant updateRestaurant(UUID id, Restaurant restaurant) {
        Restaurant existingRestaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));

        Restaurant restaurantToUpdate = new Restaurant(existingRestaurant.id(), restaurant.name(), restaurant.description(),
                restaurant.workingTimes(), restaurant.exceptionalClosingTimes(), restaurant.capacity());

        return restaurantRepository.save(restaurantToUpdate);
    }
}
