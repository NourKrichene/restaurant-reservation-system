package com.nour.reservation_manager.adapter.out.rest.mapper;


import com.nour.reservation_manager.adapter.out.rest.dto.WorkingTimeDto;
import com.nour.reservation_manager.domain.model.WorkingTime;

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
