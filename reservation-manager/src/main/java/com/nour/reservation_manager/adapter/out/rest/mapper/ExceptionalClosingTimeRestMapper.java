package com.nour.reservation_manager.adapter.out.rest.mapper;


import com.nour.reservation_manager.adapter.out.rest.dto.ExceptionalClosingTimeDto;
import com.nour.reservation_manager.domain.model.ExceptionalClosingTime;

public class ExceptionalClosingTimeRestMapper {
    private ExceptionalClosingTimeRestMapper() {
    }

    public static ExceptionalClosingTime toDomain(ExceptionalClosingTimeDto exceptionalClosingTimeDto) {
        return new ExceptionalClosingTime(exceptionalClosingTimeDto.id(), exceptionalClosingTimeDto.startTime(), exceptionalClosingTimeDto.endTime(), exceptionalClosingTimeDto.reason());
    }

    public static ExceptionalClosingTimeDto toDto(ExceptionalClosingTime exceptionalClosingTime) {
        return new ExceptionalClosingTimeDto(exceptionalClosingTime.id(), exceptionalClosingTime.startTime(), exceptionalClosingTime.endTime(), exceptionalClosingTime.reason());
    }
}
