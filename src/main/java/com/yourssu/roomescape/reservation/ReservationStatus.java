package com.yourssu.roomescape.reservation;

public enum ReservationStatus {
    RESERVED("예약"),
    CANCELED("취소"),
    COMPLETED("완료"),
    WAITING("예약대기");

    private final String value;

    ReservationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getWaitingStatusWithRank(long rank) {
        if (this == WAITING) {
            return rank + "번째 " + this.value;
        }
        return this.value;
    }
}
