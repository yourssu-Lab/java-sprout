package com.yourssu.roomescape.reservation.waiting;

public record WaitingResponse(Long id, int waitingNumber) {

    public static WaitingResponse of(Waiting waiting, int waitingNumber) {
        return new WaitingResponse(waiting.getId(), waitingNumber);
    }
}

