package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;

public class ReservationResponse {
    private Long id;
    private String name;
    private String theme;
    private String date;
    private String time;

    private ThemeRepository themeRepository;
    private TimeRepository timeRepository;
    private ReservationResponse reservationRepository;


    public ReservationResponse(Long id, String name, String theme, String date, String time) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
