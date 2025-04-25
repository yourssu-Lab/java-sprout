package com.yourssu.roomescape.theme;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Theme() {}

    public Theme(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Theme(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void markDeleted() {
        this.deleted = true;
    }
}
