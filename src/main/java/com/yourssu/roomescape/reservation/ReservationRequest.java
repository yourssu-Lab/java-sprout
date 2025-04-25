package com.yourssu.roomescape.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ReservationRequest {
    private final String name; // Optional: 관리자만 사용

    @NotBlank(message = "예약 날짜는 필수입니다.")
    private final String date;

    @NotNull(message = "예약 시간은 필수입니다.")
    private final Long time;

    @NotNull(message = "테마 ID는 필수입니다.")
    private final Long theme;

}
