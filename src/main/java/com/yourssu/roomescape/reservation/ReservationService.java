package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.exception.MemberNotFoundException;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.reservation.waiting.Waiting;
import com.yourssu.roomescape.reservation.waiting.WaitingRepository;
import com.yourssu.roomescape.reservation.waiting.WaitingRequest;
import com.yourssu.roomescape.reservation.waiting.WaitingResponse;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            MemberRepository memberRepository,
            ThemeRepository themeRepository,
            TimeRepository timeRepository,
            WaitingRepository waitingRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        Member member = (request.name() != null && !request.name().isBlank())
                ? memberRepository.findByName(request.name())
                .orElseThrow(() -> new MemberNotFoundException("이름이 일치하는 회원이 없습니다."))
                : memberRepository.findByEmail(loginMember.email())
                .orElseThrow(() -> new MemberNotFoundException("이메일이 일치하는 회원이 없습니다."));

        Time time = timeRepository.findById(request.time()).orElseThrow();
        Theme theme = themeRepository.findById(request.theme()).orElseThrow();

        Reservation reservation = new Reservation(member, request.date(), time, theme);
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
                        it.getMember().getName(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getTimeValue()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        Long memberId = loginMember.id();

        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(memberId).stream()
                .map(MyReservationResponse::of)
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWithRankByMemberId(memberId).stream()
                .map(MyReservationResponse::fromWaiting)
                .toList();

        List<MyReservationResponse> result = new ArrayList<>(reservations);
        result.addAll(waitings);
        return result;
    }

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest request, LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.email())
                .orElseThrow(() -> new MemberNotFoundException("이메일이 일치하는 회원이 없습니다."));

        Time time = timeRepository.findById(request.time()).orElseThrow();
        Theme theme = themeRepository.findById(request.theme()).orElseThrow();

        validateNotDuplicateWaiting(member.getId(), request.date(), time, theme);

        Waiting waiting = new Waiting(member, request.date(), time, theme);
        Waiting savedWaiting = waitingRepository.save(waiting);

        int rank = waitingRepository.countByThemeAndDateAndTimeAndIdLessThan(
                theme, request.date(), time, savedWaiting.getId()
        ) + 1;

        return WaitingResponse.of(savedWaiting, rank);
    }

    private void validateNotDuplicateWaiting(Long memberId, String date, Time time, Theme theme) {
        boolean exists = waitingRepository.existsByMemberIdAndDateAndTimeAndTheme(memberId, date, time, theme);
        if (exists) throw new IllegalArgumentException("이미 예약 대기 중입니다.");
    }
}
