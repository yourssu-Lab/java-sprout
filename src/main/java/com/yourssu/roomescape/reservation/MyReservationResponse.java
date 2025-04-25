package com.yourssu.roomescape.reservation;

public record MyReservationResponse(
        Long reservationId,
        String theme,
        String date,
        String time,
        String status
) {
    public static MyReservationResponse of(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getTime_value(),
                "예약"
        );
    }
}

