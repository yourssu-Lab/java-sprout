package com.yourssu.roomescape.theme;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    boolean existsByName(String name);
}
