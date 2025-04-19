package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.LoginMember;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yourssu.roomescape.exception.ExceptionMessage.*;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private MemberRepository memberRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_TIME.getMessage()));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_THEME.getMessage()));

        Member findMember;

        if (reservationRequest.getName() == null) {
            findMember = memberRepository.findById(loginMember.getId())
                    .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_MEMBER.getMessage()));
        } else {
            findMember = memberRepository.findByName(reservationRequest.getName());
        }

        Reservation reservation = reservationRepository.save(new Reservation(findMember, reservationRequest.getDate(), time, theme));

        return new ReservationResponse(reservation.getId(), findMember.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getMember().getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MineReservationResponse> reservationMine(LoginMember loginMember){
        Member findMember = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_MEMBER.getMessage()));

        return reservationRepository.findByMember(findMember).stream()
                .map(it -> new MineReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getValue(), "예약"))
                .toList();
    }
}
