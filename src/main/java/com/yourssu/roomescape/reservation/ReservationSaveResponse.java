package com.yourssu.roomescape.reservation;

public record ReservationSaveResponse(Long id, String name, String theme, String date, String time, ReservationStatus status) {}
