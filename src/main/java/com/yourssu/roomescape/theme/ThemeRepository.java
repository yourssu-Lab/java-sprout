package com.yourssu.roomescape.theme;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findByDeletedFalse();
}
