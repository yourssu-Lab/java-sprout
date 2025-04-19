package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.reservation.enums.ReservationStatus;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.time.Time;
import jakarta.persistence.*;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String date;

    @ManyToOne
    @JoinColumn(name = "time_id")
    private Time time;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation(Member member, String date, Time time, Theme theme, ReservationStatus status) {
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.status = status;
    }

    public Reservation() {

    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
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

    public ReservationStatus getStatus(){
        return status;
    }
}