package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMember(Member member);

    List<Reservation> findByMemberAndStatus(Member member, ReservationStatus status);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    @Query("SELECT new com.yourssu.roomescape.reservation.ReservationWaitingWithRank(" +
            "    r, " +
            "    (SELECT COUNT(r2) " +
            "     FROM Reservation r2 " +
            "     WHERE r2.theme = r.theme " +
            "       AND r2.date = r.date " +
            "       AND r2.time = r.time " +
            "       AND r2.status = 'WAITING' " +
            "       AND r2.id < r.id)) " +
            "FROM Reservation r " +
            "WHERE r.member = :member " +
            "AND r.status = 'WAITING'"
    )
    List<ReservationWaitingWithRank> findWaitingsWithRankByMember(Member member);
}
