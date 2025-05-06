package com.yourssu.roomescape.reservation.waiting;

import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.time.Time;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    boolean existsByMemberIdAndDateAndTimeAndTheme(Long memberId, String date, Time time, Theme theme);

    @Query("""
                SELECT new com.yourssu.roomescape.reservation.waiting.WaitingWithRank(
                    w,
                    CAST(
                        (SELECT COUNT(w2)
                         FROM Waiting w2
                         WHERE w2.theme = w.theme
                         AND w2.date = w.date
                         AND w2.time = w.time
                         AND w2.id < w.id
                    ) AS int) + 1)
                FROM Waiting w
                WHERE w.member.id = :memberId
            """)
    @EntityGraph(attributePaths = {"theme", "time"})
    List<WaitingWithRank> findWithRankByMemberId(@Param("memberId") Long memberId);

    @Query("""
            SELECT COUNT(w)
            FROM Waiting w
            WHERE w.theme = :theme
              AND w.date = :date
              AND w.time = :time
              AND w.id < :id
            """)
    int countByThemeAndDateAndTimeAndIdLessThan(
            @Param("theme") Theme theme,
            @Param("date") String date,
            @Param("time") Time time,
            @Param("id") Long id
    );
}
