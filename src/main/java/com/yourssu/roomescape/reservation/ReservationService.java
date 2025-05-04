package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.UserInfo;
import com.yourssu.roomescape.common.exception.ResourceNotFoundException;
import com.yourssu.roomescape.common.exception.UnauthorizedException;
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
    public ReservationResponse save(ReservationRequest reservationRequest, UserInfo userInfo) {
        String name;
        Member member = null;

        if (userInfo != null) {
            member = memberRepository.findById(userInfo.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
            name = userInfo.getName();
        } else if (reservationRequest.name() != null) {
            name = reservationRequest.name();
        } else {
            throw new UnauthorizedException("login is required");
        }

        Time time = timeRepository.findById(reservationRequest.time())
                .orElseThrow(() -> new ResourceNotFoundException("Time not found"));
        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow(() -> new ResourceNotFoundException("Theme not found"));

        Reservation reservation = new Reservation(
                reservationRequest.date(),
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
                        ReservationStatus.RESERVED.getValue()
                ))
                .collect(Collectors.toList());

        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(member.getId());

        List<MyReservationResponse> waitingResponses = waitings.stream()
                .map(wr -> new MyReservationResponse(
                        wr.getWaiting().getId(),
                        wr.getWaiting().getTheme().getName(),
                        wr.getWaiting().getDate(),
                        wr.getWaiting().getTime().getValue(),
                        ReservationStatus.WAITING.getWaitingStatusWithRank(wr.getRank())
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
