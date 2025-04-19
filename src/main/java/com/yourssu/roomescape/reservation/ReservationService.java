package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.LoginMember;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberFinder;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.reservation.enums.ReservationStatus;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeFinder;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeFinder;
import com.yourssu.roomescape.time.TimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yourssu.roomescape.exception.ExceptionMessage.*;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private MemberRepository memberRepository;
    private MemberFinder memberFinder;
    private ThemeFinder themeFinder;
    private TimeFinder timeFinder;
    private ReservationFinder reservationFinder;


    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, MemberFinder memberFinder, ThemeFinder themeFinder, TimeFinder timeFinder, ReservationFinder reservationFinder) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.memberFinder = memberFinder;
        this.timeFinder = timeFinder;
        this.themeFinder = themeFinder;
        this.reservationFinder = reservationFinder;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Time time = timeFinder.findTime(reservationRequest.getTime());
        Theme theme = themeFinder.findTheme(reservationRequest.getTheme());

        Member findMember;

        if (reservationRequest.getName() == null) {
            findMember = memberFinder.findMember(loginMember);
        } else {
            findMember = memberRepository.findByName(reservationRequest.getName());
        }

        Reservation reservation = reservationRepository.save(new Reservation(findMember, reservationRequest.getDate(), time, theme, ReservationStatus.RESERVATION));

        return new ReservationResponse(reservation.getId(), findMember.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        Reservation findReservation = reservationFinder.findReservation(id);

        if (findReservation.getStatus() == ReservationStatus.WAITING)
            reservationRepository.deleteById(id);
        else {
            throw new IllegalStateException("예약은 대기 상태일 때만 취소할 수 있습니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getMember().getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MineReservationResponse> reservationMine(LoginMember loginMember){
        Member findMember = memberFinder.findMember(loginMember);

        return reservationRepository.findByMember(findMember).stream()
                .map(it -> new MineReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getValue(), it.getStatus().getStatus()))
                .toList();
    }

    public ReservationResponse waitReservation(LoginMember loginMember, ReservationWaitingRequest request){
        Time time = timeFinder.findTime(request.time());
        Theme theme = themeFinder.findTheme(request.theme());

        Member findMember = memberFinder.findMember(loginMember);

        Reservation reservation = reservationRepository.save(new Reservation(findMember, request.date(), time, theme, ReservationStatus.RESERVATION));

        return new ReservationResponse(reservation.getId(), findMember.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());

    }
}
