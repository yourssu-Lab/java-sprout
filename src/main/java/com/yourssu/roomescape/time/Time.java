package com.yourssu.roomescape.time;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "times")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public Time(String timeValue) {
        this.timeValue = timeValue;
    }

    public Time(Long id, String timeValue) {
        this.id = id;
        this.timeValue = timeValue;
    }
}
