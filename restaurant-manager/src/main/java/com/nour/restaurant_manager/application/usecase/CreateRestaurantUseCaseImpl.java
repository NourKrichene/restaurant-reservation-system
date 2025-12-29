package com.nour.restaurant_manager.application.usecase;

import com.nour.restaurant_manager.application.port.in.CreateRestaurantUseCase;
import com.nour.restaurant_manager.application.port.out.RestaurantRepository;
import com.nour.restaurant_manager.application.port.out.IdGenerator;
import com.nour.restaurant_manager.domain.model.Restaurant;

public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final IdGenerator idGenerator;

    public CreateRestaurantUseCaseImpl(RestaurantRepository restaurantRepository, IdGenerator idGenerator) {
        this.restaurantRepository = restaurantRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        Restaurant restaurantToAdd = new Restaurant(idGenerator.generate(), restaurant.name(), restaurant.description(),
                restaurant.workingTimes(), restaurant.exceptionalClosingTimes(), restaurant.capacity());
        return restaurantRepository.save(restaurantToAdd);
    }
}
