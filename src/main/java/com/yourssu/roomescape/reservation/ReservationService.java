package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.exception.DuplicateException;
import com.yourssu.roomescape.exception.ErrorCode;
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
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND, "이름: "+ request.name()))
                : memberRepository.findByEmail(loginMember.email())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND, "이메일: " + loginMember.email()));

        Time time = timeRepository.findById(request.time()).orElseThrow();
        Theme theme = themeRepository.findById(request.theme()).orElseThrow();

        validateNotDuplicateReservation(member.getId(), request.date(), time.getId(), theme.getId());
        Reservation reservation = new Reservation(member, request.date(), time, theme);
        reservationRepository.save(reservation);

        return ReservationResponse.of(reservation);
    }

    private void validateNotDuplicateReservation(Long memberId, String date, Long timeId, Long themeId) {
        boolean exists = reservationRepository.existsByMemberIdAndDateAndTimeIdAndThemeId(memberId, date, timeId, themeId);
        if (exists) {
            throw new DuplicateException(ErrorCode.RESERVATION_ALREADY_EXISTS);
        }
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
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND, "이메일"+loginMember.email()));

        Time time = timeRepository.findById(request.time()).orElseThrow();
        Theme theme = themeRepository.findById(request.theme()).orElseThrow();

        validateNotDuplicateReservation(member.getId(), request.date(), time.getId(), theme.getId());
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
        if (exists) {
            throw new DuplicateException(ErrorCode.WAITING_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void cancelWaiting(Long waitingId, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(() -> new IllegalArgumentException("예약 대기를 찾을 수 없습니다."));

        if (!waiting.getMember().getId().equals(loginMember.id())) {
            throw new IllegalArgumentException("본인의 예약 대기만 취소할 수 있습니다.");
        }

        waitingRepository.delete(waiting);
    }

}
