package com.yourssu.roomescape.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationResponse {
    private final Long id;
    private final String name;
    private final String date;
    private final String time;
    private final String theme;

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                reservation.getTheme().getName()
        );
    }
}
