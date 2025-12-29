package com.nour.reservation_manager.adapter.out.rest.dto;

import java.time.LocalDateTime;

public record ExceptionalClosingTimeDto(Long id, LocalDateTime startTime, LocalDateTime endTime, String reason) {
}
