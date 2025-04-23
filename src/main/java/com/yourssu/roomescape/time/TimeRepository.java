package com.yourssu.roomescape.time;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<Time, Long> {
    void deleteById(Long id);
}