package com.yourssu.roomescape.reservation.dto;

import com.yourssu.roomescape.reservation.Reservation;
import com.yourssu.roomescape.reservation.ReservationStatus;

public record ReservationSaveResponse(Long id, String name, String theme, String date, String time, ReservationStatus status, Long waitingRank) {

    public static ReservationSaveResponse of(Reservation newReservation, Long waitingRank) {
        return new ReservationSaveResponse(
                newReservation.getId(),
                newReservation.getMember().getName(),
                newReservation.getTheme().getName(),
                newReservation.getDate(),
                newReservation.getTime().getValue(),
                newReservation.getStatus(),
                waitingRank
        );
    }
}
