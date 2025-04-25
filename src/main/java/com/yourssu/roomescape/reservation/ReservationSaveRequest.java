package com.yourssu.roomescape.reservation;

public record ReservationSaveRequest(String name, String date, Long theme, Long time, ReservationStatus status) { }
