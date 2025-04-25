package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    private Theme theme;

    protected Reservation() {}

    public Reservation(String name, String date, Time time, Theme theme) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Member member, String date, Time time, Theme theme) {
        this.member = member;
        this.name = member.getName();
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

}
