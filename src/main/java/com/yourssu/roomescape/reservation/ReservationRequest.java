package com.yourssu.roomescape.reservation;
import jakarta.validation.constraints.NotNull;


public class ReservationRequest {
    private String name;
    @NotNull(message="date is required")
    private String date;
    @NotNull(message="theme is required")
    private Long theme;
    @NotNull(message="time is required")
    private Long time;

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Long getTheme() {
        return theme;
    }

    public Long getTime() {
        return time;
    }
}
