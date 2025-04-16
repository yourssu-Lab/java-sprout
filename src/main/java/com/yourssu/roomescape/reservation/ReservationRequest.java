package com.yourssu.roomescape.reservation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ReservationRequest {
    private final String name; // Optional: 관리자만 사용
    private final String date;
    private final Long time;
    private final Long theme;
}
