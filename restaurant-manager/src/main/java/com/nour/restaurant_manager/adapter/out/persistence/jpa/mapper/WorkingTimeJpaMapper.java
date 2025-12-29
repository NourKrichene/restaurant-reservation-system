package com.nour.restaurant_manager.adapter.out.persistence.jpa.mapper;

import com.nour.restaurant_manager.adapter.out.persistence.jpa.entity.WorkingTimeJpaEntity;
import com.nour.restaurant_manager.domain.model.WorkingTime;

public class WorkingTimeJpaMapper {

    private WorkingTimeJpaMapper() {
    }

    public static WorkingTime toDomain(WorkingTimeJpaEntity workingTimeJpaEntity) {
        return new WorkingTime(
                workingTimeJpaEntity.getId(),
                workingTimeJpaEntity.getDayOfWeek(),
                workingTimeJpaEntity.getOpenTime(),
                workingTimeJpaEntity.getCloseTime()
        );
    }

    public static WorkingTimeJpaEntity toEntity(WorkingTime workingTime) {
        WorkingTimeJpaEntity jpaEntity = new WorkingTimeJpaEntity();
        if (workingTime.id() != null) {
            jpaEntity.setId(workingTime.id());
        }
        jpaEntity.setDayOfWeek(workingTime.dayOfWeek());
        jpaEntity.setOpenTime(workingTime.openTime());
        jpaEntity.setCloseTime(workingTime.closeTime());
        return jpaEntity;
    }
}
