package com.yourssu.roomescape.time;

import org.springframework.stereotype.Component;

import static com.yourssu.roomescape.exception.ExceptionMessage.NO_EXIST_TIME;

@Component
public class TimeFinder {

    private final TimeRepository timeRepository;

    public TimeFinder(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public Time findTime(Long time){
        return timeRepository.findById(time)
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_TIME.getMessage()));
    }
}
