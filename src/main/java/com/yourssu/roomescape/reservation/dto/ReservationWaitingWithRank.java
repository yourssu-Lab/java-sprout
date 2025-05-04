package com.yourssu.roomescape.reservation.dto;

import com.yourssu.roomescape.reservation.Reservation;

public record ReservationWaitingWithRank(Reservation reservation, Long rank) {}
