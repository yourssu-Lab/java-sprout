package com.yourssu.roomescape.reservation.dto;

import com.yourssu.roomescape.reservation.ReservationStatus;
import jakarta.validation.constraints.NotNull;

public record ReservationSaveRequest(
        String name,
        @NotNull String date,
        @NotNull Long theme,
        @NotNull Long time,
        @NotNull ReservationStatus status
) { }
