package com.yourssu.roomescape.reservation.dto;

import com.yourssu.roomescape.reservation.ReservationStatus;

public record ReservationSaveRequest(String name, String date, Long theme, Long time, ReservationStatus status) { }
