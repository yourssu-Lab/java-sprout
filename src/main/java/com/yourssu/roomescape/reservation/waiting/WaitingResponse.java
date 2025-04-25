package com.yourssu.roomescape.reservation.waiting;

import lombok.Getter;

@Getter
public class WaitingResponse {

    private final Long id;

    public WaitingResponse(Long id) {
        this.id = id;
    }

    public static WaitingResponse of(Waiting waiting) {
        return new WaitingResponse(waiting.getId());
    }
}
