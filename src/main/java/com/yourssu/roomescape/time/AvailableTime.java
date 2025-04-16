package com.yourssu.roomescape.time;

import lombok.Getter;

@Getter
public class AvailableTime {
    private Long timeId;
    private String time;
    private boolean booked;

    public AvailableTime(Long timeId, String time, boolean booked) {
        this.timeId = timeId;
        this.time = time;
        this.booked = booked;
    }

}
