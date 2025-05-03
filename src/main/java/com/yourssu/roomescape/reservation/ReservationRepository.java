package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
    List<Reservation> findByMember(Member member);
    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
            "WHERE r.member.id = :memberId " +
            "AND r.date = :date " +
            "AND r.theme.id = :themeId " +
            "AND r.time.id = :timeId")
    boolean existsByMemberIdAndDateAndThemeIdAndTimeId(
            @Param("memberId") Long memberId,
            @Param("date") String date,
            @Param("themeId") Long themeId,
            @Param("timeId") Long timeId);

}
