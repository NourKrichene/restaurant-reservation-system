package com.nour.restaurant_manager.adapter.out.persistence.jpa;

import com.nour.restaurant_manager.adapter.out.persistence.jpa.entity.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataRestaurantRepository extends JpaRepository<RestaurantJpaEntity, UUID> {
}
