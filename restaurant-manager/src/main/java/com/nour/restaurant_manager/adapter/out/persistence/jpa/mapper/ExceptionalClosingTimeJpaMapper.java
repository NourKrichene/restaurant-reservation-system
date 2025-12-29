package com.nour.restaurant_manager.adapter.out.persistence.jpa.mapper;

import com.nour.restaurant_manager.adapter.out.persistence.jpa.entity.ExceptionalClosingTimeJpaEntity;
import com.nour.restaurant_manager.domain.model.ExceptionalClosingTime;

public class ExceptionalClosingTimeJpaMapper {
    private ExceptionalClosingTimeJpaMapper() {
    }

    public static ExceptionalClosingTime toDomain(ExceptionalClosingTimeJpaEntity entity) {
        return new ExceptionalClosingTime(
                entity.getId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getReason()
        );
    }

    public static ExceptionalClosingTimeJpaEntity toEntity(ExceptionalClosingTime exceptionalClosingTime) {
        ExceptionalClosingTimeJpaEntity jpaEntity = new ExceptionalClosingTimeJpaEntity();
        if (exceptionalClosingTime.id() != null) {
            jpaEntity.setId(exceptionalClosingTime.id());
        }
        jpaEntity.setStartTime(exceptionalClosingTime.startTime());
        jpaEntity.setEndTime(exceptionalClosingTime.endTime());
        jpaEntity.setReason(exceptionalClosingTime.reason());
        return jpaEntity;
    }
}
