package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberService;
import com.yourssu.roomescape.theme.ThemeDao;
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
        Member member = (request.getName() != null && !request.getName().isBlank())
                ? memberService.findByName(request.getName())
                : memberService.findByEmail(loginMember.getEmail());

        ReservationRequest filledRequest = request.withName(member.getName());
        Reservation reservation = reservationDao.save(filledRequest);

        return ReservationResponse.of(reservation);
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
