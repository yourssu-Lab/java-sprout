package com.yourssu.roomescape.reservation.waiting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingResponse {

    private final Long id;
    private final int waitingNumber;

    public static WaitingResponse of(Waiting waiting, int waitingNumber) {
        return new WaitingResponse(waiting.getId(), waitingNumber);
    }
}

