package com.yourssu.roomescape.reservation;

public class ReservationRequest {
    private String date;
    private Long theme;
    private Long time;
    private String name;

    public String getDate() {
        return date;
    }

    public Long getTheme() {
        return theme;
    }

    public Long getTime() {
        return time;
    }
    public String getName(){return name;}
}
