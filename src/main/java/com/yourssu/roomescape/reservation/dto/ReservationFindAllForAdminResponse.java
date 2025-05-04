package com.yourssu.roomescape.reservation.dto;

import com.yourssu.roomescape.reservation.Reservation;

public record ReservationFindAllForAdminResponse(Long reservationId, String name, String theme, String date, String time) {

    public static ReservationFindAllForAdminResponse from(Reservation newReservation) {
        return new ReservationFindAllForAdminResponse(
                newReservation.getId(),
                newReservation.getMember().getName(),
                newReservation.getTheme().getName(),
                newReservation.getDate(),
                newReservation.getTime().getValue()
        );
    }
}
