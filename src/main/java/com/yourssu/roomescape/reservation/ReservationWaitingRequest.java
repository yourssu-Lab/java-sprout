package com.yourssu.roomescape.reservation;

public record ReservationWaitingRequest(
        String date,
        Long time,
        Long theme
) {
}
