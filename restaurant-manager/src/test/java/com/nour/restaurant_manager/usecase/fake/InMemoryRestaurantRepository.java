package com.nour.restaurant_manager.usecase.fake;

import com.nour.restaurant_manager.application.port.out.RestaurantRepository;
import com.nour.restaurant_manager.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRestaurantRepository implements RestaurantRepository {

    private final Map<UUID, Restaurant> store = new ConcurrentHashMap<>();

    @Override
    public Restaurant save(Restaurant restaurant) {
        store.put(restaurant.id(), restaurant);
        return restaurant;
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Page<Restaurant> findAll(Pageable pageable) {
        List<Restaurant> restaurants = new ArrayList<>(store.values());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), restaurants.size());
        List<Restaurant> pageContent = restaurants.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, restaurants.size());
    }

                                    @Override
    public boolean delete(UUID id) {
        return store.remove(id) != null;
    }
}
