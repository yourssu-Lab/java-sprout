package com.yourssu.roomescape.reservation;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        String name,

        @NotNull(message="date is required")
        String date,

        @NotNull(message="theme is required")
        Long theme,

        @NotNull(message="time is required")
        Long time
) {
}
