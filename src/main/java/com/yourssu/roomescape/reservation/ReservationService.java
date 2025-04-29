package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
import com.yourssu.roomescape.common.exception.ResourceNotFoundException;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import com.yourssu.roomescape.waiting.WaitingRepository;
import com.yourssu.roomescape.waiting.WaitingWithRank;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository,
                              MemberRepository memberRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = reservationRequest.getName() != null ?
                reservationRequest.getName() : loginMember.getName();

        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new ResourceNotFoundException("Time not found"));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        Member member = null;
        if (loginMember != null) {
            member = memberRepository.findById(loginMember.getId())
                    .orElse(null);
        }

        Reservation reservation = new Reservation(
                reservationRequest.getDate(),
                time,
                theme,
                member
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(
                savedReservation.getId(),
                name,
                theme.getName(),
                savedReservation.getDate(),
                time.getValue()
        );
    }

    @Transactional
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
                        it.getTime().getValue()
                ))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(Member member) {
        List<MyReservationResponse> reservations = reservationRepository.findByMember(member).stream()
                .map(it -> new MyReservationResponse(
                        it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue(),
                        "예약"
                ))
                .toList();

        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(member.getId());

        List<MyReservationResponse> waitingResponses = waitings.stream()
                .map(wr -> new MyReservationResponse(
                        wr.getWaiting().getId(),
                        wr.getWaiting().getTheme().getName(),
                        wr.getWaiting().getDate(),
                        wr.getWaiting().getTime().getValue(),
                        wr.getRank() + "번째 예약대기"
                ))
                .toList();

        reservations.addAll(waitingResponses);

        return reservations;
    }

    public List<MyReservationResponse> findMyReservationsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        return findMyReservations(member);
    }
}
