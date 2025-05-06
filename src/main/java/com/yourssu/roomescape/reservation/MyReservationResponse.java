package com.yourssu.roomescape.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yourssu.roomescape.reservation.waiting.Waiting;
import com.yourssu.roomescape.reservation.waiting.WaitingWithRank;

public record MyReservationResponse(
        @JsonProperty("id") Long Id,
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
                reservation.getTime().getTimeValue(),
                "예약"
        );
    }

    public static MyReservationResponse fromWaiting(WaitingWithRank waitingWithRank) {
        Waiting waiting = waitingWithRank.waiting();
        String status = waitingWithRank.rank() + "번째 예약대기";
        return new MyReservationResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getTimeValue(),
                status
        );
    }
}
