package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.LoginMemberInfo;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.time.Time;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ReservationService {
    private final MemberDao memberDao;
    public ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public ReservationResponse save(ReservationRequest request, LoginMemberInfo loginMember) {
        Member member;

        if (request.getName() != null && !request.getName().isBlank()) {
            // name으로 회원 조회
            member = reservationDao.findMemberByName(request.getName());
        } else {
            // 로그인된 회원 정보로 조회
            log.info("로그인된 회원 정보로 예약 진행 - 회원 ID: {}", loginMember.getId());
            member = reservationDao.findMemberById(loginMember.getId());
        }

        Theme theme = reservationDao.findThemeById(request.getTheme());
        Time time = reservationDao.findTimeById(request.getTime());

        Reservation reservation = new Reservation(member.getName(), request.getDate(), theme, time);
        Reservation saved = reservationDao.save(reservation);

        return new ReservationResponse(
                saved.getId(),
                saved.getName(),
                saved.getTheme().getName(),
                saved.getDate(),
                saved.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public Time getTimeById(Long id) {
        return reservationDao.findTimeById(id);
    }

    public Theme getThemeById(Long id) {
        return reservationDao.findThemeById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}