package com.yourssu.roomescape.time;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "times")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_value", nullable = false)
    private String timeValue;

    @Column(nullable = false)
    private boolean deleted = false;

    public Time(String timeValue) {
        this.timeValue = timeValue;
    }

    public Time(Long id, String timeValue) {
        this.id = id;
        this.timeValue = timeValue;
    }

    public void markDeleted() {
        this.deleted = true;
    }
}
