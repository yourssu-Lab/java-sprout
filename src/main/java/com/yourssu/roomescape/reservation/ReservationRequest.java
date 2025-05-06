package com.yourssu.roomescape.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(

        String name, // Optional: 관리자만 사용

        @NotBlank(message = "예약 날짜는 필수입니다.")
        String date,

        @NotNull(message = "예약 시간은 필수입니다.")
        Long time,

        @NotNull(message = "테마 ID는 필수입니다.")
        Long theme
) {
}
