package com.yourssu.roomescape.time;

import com.yourssu.roomescape.reservation.Reservation;
import jakarta.persistence.*;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column
    private String value;

    public Time(Long id, String value, Reservation reservation) {
        this.id = id;
        this.value = value;
    }

    public Time(String value) {
        this.value = value;
    }

    public Time() {

    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
