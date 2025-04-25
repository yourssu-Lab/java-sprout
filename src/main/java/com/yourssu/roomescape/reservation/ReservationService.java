package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberService;
import com.yourssu.roomescape.reservation.waiting.Waiting;
import com.yourssu.roomescape.reservation.waiting.WaitingRepository;
import com.yourssu.roomescape.reservation.waiting.WaitingRequest;
import com.yourssu.roomescape.reservation.waiting.WaitingResponse;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            MemberService memberService,
            ThemeRepository themeRepository,
            TimeRepository timeRepository,
            WaitingRepository waitingRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        Member member = (request.getName() != null && !request.getName().isBlank())
                ? memberService.findByName(request.getName())
                : memberService.findByEmail(loginMember.getEmail());

        Time time = timeRepository.findById(request.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(request.getTheme()).orElseThrow();

        Reservation reservation;
        if (request.getName() != null && !request.getName().isBlank()) {
            reservation = new Reservation(request.getName(), request.getDate(), time, theme);
        } else {
            reservation = new Reservation(member, request.getDate(), time, theme);
        }
        reservationRepository.save(reservation);
        return ReservationResponse.of(reservation);
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

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        Long memberId = loginMember.getId();

        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(memberId).stream()
                .map(MyReservationResponse::of)
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWithRankByMemberId(memberId).stream()
                .map(MyReservationResponse::fromWaiting)
                .toList();

        return Stream.concat(reservations.stream(), waitings.stream()).toList();
    }

    public WaitingResponse createWaiting(WaitingRequest request, LoginMember loginMember) {
        Member member = memberService.findByEmail(loginMember.getEmail());

        Time time = timeRepository.findById(request.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(request.getTheme()).orElseThrow();

        validateNotDuplicateWaiting(member.getId(), request.getDate(), time, theme);

        Waiting waiting = new Waiting(member, request.getDate(), time, theme);
        return WaitingResponse.of(waitingRepository.save(waiting));
    }

    private void validateNotDuplicateWaiting(Long memberId, String date, Time time, Theme theme) {
        boolean exists = waitingRepository.existsByMemberIdAndDateAndTimeAndTheme(memberId, date, time, theme);
        if (exists) throw new IllegalArgumentException("이미 예약 대기 중입니다.");
    }
}
