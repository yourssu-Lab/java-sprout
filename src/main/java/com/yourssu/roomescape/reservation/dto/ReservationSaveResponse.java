package com.yourssu.roomescape.reservation.dto;

import com.yourssu.roomescape.reservation.ReservationStatus;

public record ReservationSaveResponse(Long id, String name, String theme, String date, String time, ReservationStatus status) {}
