package com.nour.restaurant_manager.adapter.out.persistence.jpa;

import com.nour.restaurant_manager.adapter.out.persistence.jpa.mapper.RestaurantJpaMapper;
import com.nour.restaurant_manager.application.port.out.RestaurantRepository;
import com.nour.restaurant_manager.domain.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public class RestaurantRepositoryAdapter implements RestaurantRepository {

    private final SpringDataRestaurantRepository jpaRepository;

    public RestaurantRepositoryAdapter(SpringDataRestaurantRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<Restaurant> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(RestaurantJpaMapper::toDomain);
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return jpaRepository.findById(id).map(RestaurantJpaMapper::toDomain);
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        var entity = RestaurantJpaMapper.toEntity(restaurant);
        var saved = jpaRepository.save(entity);
        return RestaurantJpaMapper.toDomain(saved);
    }

    @Override
    public boolean delete(UUID id) {
        if (!jpaRepository.existsById(id)) return false;
        jpaRepository.deleteById(id);
        return true;
    }
}
