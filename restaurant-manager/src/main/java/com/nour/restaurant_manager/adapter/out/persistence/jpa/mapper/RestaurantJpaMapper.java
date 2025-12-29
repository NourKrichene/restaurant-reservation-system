package com.nour.restaurant_manager.adapter.out.persistence.jpa.mapper;

import com.nour.restaurant_manager.adapter.out.persistence.jpa.entity.ExceptionalClosingTimeJpaEntity;
import com.nour.restaurant_manager.adapter.out.persistence.jpa.entity.RestaurantJpaEntity;
import com.nour.restaurant_manager.adapter.out.persistence.jpa.entity.WorkingTimeJpaEntity;
import com.nour.restaurant_manager.domain.model.ExceptionalClosingTime;
import com.nour.restaurant_manager.domain.model.Restaurant;
import com.nour.restaurant_manager.domain.model.WorkingTime;

import java.util.List;

public class RestaurantJpaMapper {

    private RestaurantJpaMapper() {
    }

    public static Restaurant toDomain(RestaurantJpaEntity restaurantJpaEntity) {
        List<WorkingTime> workingTimes = restaurantJpaEntity.getWorkingTimes().stream().map(WorkingTimeJpaMapper::toDomain).toList();
        List<ExceptionalClosingTime> exceptionalClosingTimes = restaurantJpaEntity.getExceptionalClosingTimes().stream().map(ExceptionalClosingTimeJpaMapper::toDomain).toList();
        return new Restaurant(restaurantJpaEntity.getId(), restaurantJpaEntity.getName(), restaurantJpaEntity.getDescription(), workingTimes, exceptionalClosingTimes, restaurantJpaEntity.getCapacity());
    }

    public static RestaurantJpaEntity toEntity(Restaurant restaurant) {
        List<WorkingTimeJpaEntity> workingTimeJpaEntities = restaurant.workingTimes().stream().map(WorkingTimeJpaMapper::toEntity).toList();
        List<ExceptionalClosingTimeJpaEntity> exceptionalClosingTimeJpaEntities = restaurant.exceptionalClosingTimes().stream().map(ExceptionalClosingTimeJpaMapper::toEntity).toList();
        return new RestaurantJpaEntity(restaurant.id(), restaurant.name(), restaurant.description(), workingTimeJpaEntities, exceptionalClosingTimeJpaEntities, restaurant.capacity());
    }
}
