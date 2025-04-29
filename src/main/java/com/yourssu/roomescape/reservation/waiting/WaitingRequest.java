package com.yourssu.roomescape.reservation.waiting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WaitingRequest(

        @NotBlank
        String date,

        @NotNull
        Long time,

        @NotNull
        Long theme
) {
}
