package com.yourssu.roomescape.reservation;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByMemberId(Long memberId);

    @NotNull
    @EntityGraph(attributePaths = {"theme", "time"})
    List<Reservation> findAll();

}
