package com.nour.reservation_manager.adapter.out.rest.mapper;

import com.nour.reservation_manager.adapter.out.rest.dto.ExceptionalClosingTimeDto;
import com.nour.reservation_manager.adapter.out.rest.dto.RestaurantDto;
import com.nour.reservation_manager.adapter.out.rest.dto.WorkingTimeDto;
import com.nour.reservation_manager.domain.model.ExceptionalClosingTime;
import com.nour.reservation_manager.domain.model.Restaurant;
import com.nour.reservation_manager.domain.model.WorkingTime;

import java.util.Collections;
import java.util.List;

public class RestaurantRestMapper {

    private RestaurantRestMapper() {
    }

    public static Restaurant toDomain(RestaurantDto restaurantDto) {
        List<WorkingTime> workingTimes = restaurantDto.workingTimes() != null ? restaurantDto.workingTimes().stream().map(WorkingTimeRestMapper::toDomain).toList() : Collections.emptyList();

        List<ExceptionalClosingTime> exceptionalClosingTimes = restaurantDto.exceptionalClosingTimes() != null ? restaurantDto.exceptionalClosingTimes().stream().map(ExceptionalClosingTimeRestMapper::toDomain).toList() : Collections.emptyList();

        return new Restaurant(null, workingTimes, exceptionalClosingTimes, restaurantDto.capacity());
    }

    public static RestaurantDto toResponse(Restaurant restaurant) {
        List<WorkingTimeDto> workingTimeDtos = restaurant.workingTimes().stream().map(WorkingTimeRestMapper::toDto).toList();

        List<ExceptionalClosingTimeDto> exceptionalClosingTimes = restaurant.exceptionalClosingTimes().stream().map(ExceptionalClosingTimeRestMapper::toDto).toList();

        return new RestaurantDto(restaurant.id(), workingTimeDtos, exceptionalClosingTimes, restaurant.capacity());
    }
}
