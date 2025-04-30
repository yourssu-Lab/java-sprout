package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.time.Time;
import jakarta.persistence.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String name;  // name 필드 추가


    public Reservation(Long id, String date, Time time, Theme theme) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String date, Time time, Theme theme, Member member) {
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;

        if (member != null) {
            this.name = member.getName();
        }
    }

    public Reservation() {

    }

    public Long getId() {
        return id;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getName() {
        return member.getName();
    }
}
