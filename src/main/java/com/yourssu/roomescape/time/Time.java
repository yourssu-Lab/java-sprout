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
    private String time_value;

    @Setter
    @Column(nullable = false)
    private boolean deleted = false;

    protected Time() {
    }

    public Time(String time_value) {
        this.time_value = time_value;
    }

    public Time(Long id, String time_value) {
        this.id = id;
        this.time_value = time_value;
    }
}
