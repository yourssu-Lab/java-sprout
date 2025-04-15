package com.yourssu.roomescape.theme;

import com.yourssu.roomescape.reservation.Reservation;
import jakarta.persistence.*;

@Entity
public class Theme {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private String description;

    public Theme() {
    }

    public Theme(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Theme(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
