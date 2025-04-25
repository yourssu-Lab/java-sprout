package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberService;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            MemberService memberService,
            ThemeRepository themeRepository,
            TimeRepository timeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        Member member = (request.getName() != null && !request.getName().isBlank())
                ? memberService.findByName(request.getName())
                : memberService.findByEmail(loginMember.getEmail());

        ReservationRequest filledRequest = request.withName(member.getName());

        Theme theme = themeRepository.findById(filledRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        Time time = timeRepository.findById(filledRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        Reservation reservation = new Reservation(
                filledRequest.getName(),
                filledRequest.getDate(),
                time,
                theme
        );

        return ReservationResponse.of(reservationRepository.save(reservation));
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        it.getName(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getTime_value()
                ))
                .toList();
    }
}
