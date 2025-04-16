package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.exception.MemberNotFoundException;
import com.yourssu.roomescape.exception.ThemeNotFoundException;
import com.yourssu.roomescape.exception.TimeNotFoundException;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.member.MemberService;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeDao;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final MemberService memberService;
    private final ThemeDao themeDao;
    private final TimeDao timeDao;

    public ReservationService(
            ReservationDao reservationDao,
            MemberService memberService,
            ThemeDao themeDao,
            TimeDao timeDao
    ) {
        this.reservationDao = reservationDao;
        this.memberService = memberService;
        this.themeDao = themeDao;
        this.timeDao = timeDao;
    }

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        // 1. 예약자 식별 (관리자 or 본인)
        Member member;
        if (request.getName() != null && !request.getName().isBlank()) {
            member = memberService.findByName(request.getName());
        } else {
            member = memberService.findByEmail(loginMember.getEmail());
        }

        // 2. request에 name이 null일 경우 본인의 이름을 주입한 새로운 request 생성
        ReservationRequest actualRequest = new ReservationRequest(
                member.getName(),  // 실제 저장될 name
                request.getDate(),
                request.getTime(),
                request.getTheme()
        );

        // 3. DAO에 저장
        Reservation reservation = reservationDao.save(actualRequest);  // 기존 인터페이스 그대로 활용

        // 4. 응답용 Theme & Time 조회
        Theme theme = themeDao.findById(actualRequest.getTheme())
                .orElseThrow(() -> new ThemeNotFoundException("테마 정보를 찾을 수 없습니다."));
        Time time = timeDao.findById(actualRequest.getTime())
                .orElseThrow(() -> new TimeNotFoundException("시간 정보를 찾을 수 없습니다."));

        return new ReservationResponse(
                reservation.getId(),
                actualRequest.getName(),
                actualRequest.getDate(),
                time.getValue(),
                theme.getName()
        );
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
