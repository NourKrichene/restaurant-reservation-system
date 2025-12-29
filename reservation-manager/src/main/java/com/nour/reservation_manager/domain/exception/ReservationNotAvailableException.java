package com.nour.reservation_manager.domain.exception;

public class ReservationNotAvailableException extends RuntimeException {
    public ReservationNotAvailableException(String message) {
        super(message);
    }
}
