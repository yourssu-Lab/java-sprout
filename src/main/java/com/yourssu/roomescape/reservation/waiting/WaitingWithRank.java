package com.yourssu.roomescape.reservation.waiting;

import lombok.Getter;

@Getter
public class WaitingWithRank {

    private final Waiting waiting;
    private final int rank;

    public WaitingWithRank(Waiting waiting, int rank) {
        this.waiting = waiting;
        this.rank = rank;
    }
}