package com.nour.restaurant_manager.application.port.out;

import com.nour.restaurant_manager.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository {

    Page<Restaurant> findAll(Pageable pageable);

    Optional<Restaurant> findById(UUID id);

    Restaurant save(Restaurant restaurant);

    boolean delete(UUID id);
}
