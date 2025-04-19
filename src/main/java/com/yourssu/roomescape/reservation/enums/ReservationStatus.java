package com.yourssu.roomescape.reservation.enums;

public enum ReservationStatus {
    RESERVATION("예약"), WAITING("예약 대기");

    ReservationStatus(String status){
        this.status = status;
    }

    private final String status;

    public String getStatus() {
        return status;
    }
}
