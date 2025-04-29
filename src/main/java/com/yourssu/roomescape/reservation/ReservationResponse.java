package com.yourssu.roomescape.reservation;

public record ReservationResponse(Long id, String name, String date, String time, String theme) {
    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMember().getName(),
                reservation.getDate(),
                reservation.getTime().getTimeValue(),
                reservation.getTheme().getName()
        );
    }
}
