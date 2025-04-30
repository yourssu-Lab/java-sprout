package com.yourssu.roomescape.waiting;

import com.yourssu.roomescape.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    List<Waiting> findByMember(Member member);

    @Query("SELECT new com.yourssu.roomescape.waiting.WaitingWithRank(" +
            "    w, " +
            "    (SELECT COUNT(w2) " +
            "     FROM Waiting w2 " +
            "     WHERE w2.theme.id = w.theme.id " +
            "       AND w2.date = w.date " +
            "       AND w2.time.id = w.time.id " +
            "       AND w2.id < w.id) + 1) " +
            "FROM Waiting w " +
            "WHERE w.member.id = :memberId")
    List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(w) > 0 FROM Waiting w " +
            "WHERE w.member.id = :memberId " +
            "AND w.date = :date " +
            "AND w.theme.id = :themeId " +
            "AND w.time.id = :timeId")
    boolean existsByMemberIdAndDateAndThemeIdAndTimeId(
            @Param("memberId") Long memberId,
            @Param("date") String date,
            @Param("themeId") Long themeId,
            @Param("timeId") Long timeId);

    List<Waiting> findByDateAndThemeIdAndTimeId(String date, Long themeId, Long timeId);
}