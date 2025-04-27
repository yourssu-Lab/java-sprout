package com.yourssu.roomescape.time;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "times")
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "time_value", nullable = false)
    private String timeValue;

    @Setter
    @Column(nullable = false)
    private boolean deleted = false;

    protected Time() {
    }

    public Time(String timeValue) {
        this.timeValue = timeValue;
    }

    public Time(Long id, String timeValue) {
        this.id = id;
        this.timeValue = timeValue;
    }
}
