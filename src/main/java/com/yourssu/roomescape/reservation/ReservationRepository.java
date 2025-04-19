package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMember(Member member);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
