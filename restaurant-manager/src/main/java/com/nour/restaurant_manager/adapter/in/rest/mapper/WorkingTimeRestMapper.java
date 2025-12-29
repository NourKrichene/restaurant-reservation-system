package com.nour.restaurant_manager.adapter.in.rest.mapper;

import com.nour.restaurant_manager.adapter.in.rest.dto.WorkingTimeDto;
import com.nour.restaurant_manager.domain.model.WorkingTime;

public class WorkingTimeRestMapper {
    private WorkingTimeRestMapper() {
    }

    public static WorkingTime toDomain(WorkingTimeDto workingTimeDto) {
        return new WorkingTime(
                workingTimeDto.id(),
                workingTimeDto.dayOfWeek(),
                workingTimeDto.openTime(),
                workingTimeDto.closeTime()
        );
    }

    public static WorkingTimeDto toDto(WorkingTime workingTime) {
        return new WorkingTimeDto(
                workingTime.id(),
                workingTime.dayOfWeek(),
                workingTime.openTime(),
                workingTime.closeTime()
        );
    }
}
