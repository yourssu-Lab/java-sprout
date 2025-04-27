package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.reservation.waiting.Waiting;
import com.yourssu.roomescape.reservation.waiting.WaitingWithRank;
import lombok.Getter;

@Getter
public class MyReservationResponse {
    private final Long reservationId;
    private final String theme;
    private final String date;
    private final String time;
    private final String status;

    public MyReservationResponse(Long reservationId, String theme, String date, String time, String status) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

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
        Waiting waiting = waitingWithRank.getWaiting();
        String status = waitingWithRank.getRank() + "번째 예약대기";
        return new MyReservationResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getTimeValue(),
                status
        );
    }
}
