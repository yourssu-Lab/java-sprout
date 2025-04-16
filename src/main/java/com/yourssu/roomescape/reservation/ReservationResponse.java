package com.yourssu.roomescape.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationResponse {
    private final Long id;
    private final String name;
    private final String date;
    private final String time;
    private final String theme;
}
