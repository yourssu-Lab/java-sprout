package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.LoginMember;
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
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new RuntimeException("Time not found"));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        Member member = null;
        if (loginMember != null) {
            member = memberRepository.findById(loginMember.getId())
                    .orElse(null);
        }

        Reservation reservation = new Reservation(
                name,
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
        // 예약 목록 가져오기
        List<MyReservationResponse> reservations = reservationRepository.findByMember(member).stream()
                .map(it -> new MyReservationResponse(
                        it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue(),
                        "예약"
                ))
                .collect(Collectors.toList());

        // 대기 목록 가져오기 (순위 포함)
        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(member.getId());

        // 대기 목록을 MyReservationResponse로 변환하여 추가
        List<MyReservationResponse> waitingResponses = waitings.stream()
                .map(wr -> new MyReservationResponse(
                        wr.getWaiting().getId(),
                        wr.getWaiting().getTheme().getName(),
                        wr.getWaiting().getDate(),
                        wr.getWaiting().getTime().getValue(),
                        wr.getRank() + "번째 예약대기"
                ))
                .collect(Collectors.toList());

        // 예약과 대기 목록 합치기
        reservations.addAll(waitingResponses);

        return reservations;
    }
}
