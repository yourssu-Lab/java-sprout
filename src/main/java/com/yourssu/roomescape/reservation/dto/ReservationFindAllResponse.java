package com.yourssu.roomescape.reservation.dto;

import com.yourssu.roomescape.reservation.Reservation;

public record ReservationFindAllResponse(Long reservationId, String theme, String date, String time, String status) {

    public static ReservationFindAllResponse fromReservation(Reservation reservation) {
        return new ReservationFindAllResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                reservation.getStatus().getDescription()
        );
    }

    public static ReservationFindAllResponse fromWaitingWithRank(ReservationWaitingWithRank waitingWithRank) {
        return new ReservationFindAllResponse(
                waitingWithRank.reservation().getId(),
                waitingWithRank.reservation().getTheme().getName(),
                waitingWithRank.reservation().getDate(),
                waitingWithRank.reservation().getTime().getValue(),
                (waitingWithRank.rank() + 1) + "번째 예약대기"
        );
    }
}
