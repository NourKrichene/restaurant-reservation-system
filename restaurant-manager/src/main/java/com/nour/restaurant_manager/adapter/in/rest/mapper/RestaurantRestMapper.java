package com.nour.restaurant_manager.adapter.in.rest.mapper;

import com.nour.restaurant_manager.adapter.in.rest.dto.ExceptionalClosingTimeDto;
import com.nour.restaurant_manager.adapter.in.rest.dto.RestaurantRequestDto;
import com.nour.restaurant_manager.adapter.in.rest.dto.RestaurantResponseDto;
import com.nour.restaurant_manager.adapter.in.rest.dto.WorkingTimeDto;
import com.nour.restaurant_manager.domain.model.ExceptionalClosingTime;
import com.nour.restaurant_manager.domain.model.Restaurant;
import com.nour.restaurant_manager.domain.model.WorkingTime;

import java.util.Collections;
import java.util.List;

public class RestaurantRestMapper {

    private RestaurantRestMapper() {
    }

    public static Restaurant toDomain(RestaurantRequestDto restaurantRequestDto) {
        List<WorkingTime> workingTimes = restaurantRequestDto.workingTimes() != null ? restaurantRequestDto.workingTimes().stream().map(WorkingTimeRestMapper::toDomain).toList() : Collections.emptyList();

        List<ExceptionalClosingTime> exceptionalClosingTimes = restaurantRequestDto.exceptionalClosingTimes() != null ? restaurantRequestDto.exceptionalClosingTimes().stream().map(ExceptionalClosingTimeRestMapper::toDomain).toList() : Collections.emptyList();

        return new Restaurant(null, restaurantRequestDto.name(), restaurantRequestDto.description(), workingTimes, exceptionalClosingTimes, restaurantRequestDto.capacity());
    }

    public static RestaurantResponseDto toResponse(Restaurant restaurant) {
        List<WorkingTimeDto> workingTimeDtos = restaurant.workingTimes().stream().map(WorkingTimeRestMapper::toDto).toList();

        List<ExceptionalClosingTimeDto> exceptionalClosingTimes = restaurant.exceptionalClosingTimes().stream().map(ExceptionalClosingTimeRestMapper::toDto).toList();

        return new RestaurantResponseDto(restaurant.id(), restaurant.name(), restaurant.description(), workingTimeDtos, exceptionalClosingTimes, restaurant.capacity());
    }
}
