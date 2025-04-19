package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.time.Time;

public class Reservation {
    private Long id;
    private String name;
    private String date;
    private Time time;
    private Theme theme;

    public Reservation(Long id, String name, String date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, String date, Theme theme, Time time) {
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public Reservation(String date, Time time, Theme theme) {
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
