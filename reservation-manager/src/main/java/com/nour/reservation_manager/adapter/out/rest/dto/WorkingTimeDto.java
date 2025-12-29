package com.nour.reservation_manager.adapter.out.rest.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkingTimeDto(Long id, DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime) {

}
