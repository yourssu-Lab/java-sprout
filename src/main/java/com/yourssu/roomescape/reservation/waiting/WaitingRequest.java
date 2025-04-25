// WaitingRequest.java
package com.yourssu.roomescape.reservation.waiting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WaitingRequest {

    @NotBlank
    private String date;

    @NotNull
    private Long time;

    @NotNull
    private Long theme;
}
