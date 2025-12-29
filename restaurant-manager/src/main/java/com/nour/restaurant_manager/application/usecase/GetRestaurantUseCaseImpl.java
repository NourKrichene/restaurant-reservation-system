package com.nour.restaurant_manager.application.usecase;

import com.nour.restaurant_manager.application.port.in.GetRestaurantUseCase;
import com.nour.restaurant_manager.application.port.out.RestaurantRepository;
import com.nour.restaurant_manager.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GetRestaurantUseCaseImpl implements GetRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;

    public GetRestaurantUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Page<Restaurant> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }

    @Override
    public Optional<Restaurant> getRestaurantById(UUID id) {
        return restaurantRepository.findById(id);
    }
}
