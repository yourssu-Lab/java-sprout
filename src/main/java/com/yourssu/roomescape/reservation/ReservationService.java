package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.exception.CustomAlreadyReservationException;
import com.yourssu.roomescape.member.LoginMember;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberFinder;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.reservation.enums.ReservationStatus;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeFinder;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yourssu.roomescape.exception.ExceptionMessage.*;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final MemberFinder memberFinder;
    private final ThemeFinder themeFinder;
    private final TimeFinder timeFinder;
    private final ReservationFinder reservationFinder;


    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, MemberFinder memberFinder, ThemeFinder themeFinder, TimeFinder timeFinder, ReservationFinder reservationFinder) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.memberFinder = memberFinder;
        this.timeFinder = timeFinder;
        this.themeFinder = themeFinder;
        this.reservationFinder = reservationFinder;
    }

    @Transactional
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

    @Transactional
    public void deleteById(Long id) {
        Reservation findReservation = reservationFinder.findReservation(id);

        if (findReservation.getStatus() == ReservationStatus.WAITING)
            reservationRepository.deleteById(id);
        else {
            throw new IllegalStateException(CANCLE_DENY_RESERVATION.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getMember().getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MineReservationResponse> reservationMine(LoginMember loginMember){
        Member findMember = memberFinder.findMember(loginMember);

        return reservationRepository.findByMember(findMember).stream()
                .map(it -> new MineReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getValue(), it.getStatus().getStatus()))
                .toList();
    }

    @Transactional
    public ReservationResponse waitReservation(LoginMember loginMember, ReservationWaitingRequest request){
        Time time = timeFinder.findTime(request.time());
        Theme theme = themeFinder.findTheme(request.theme());

        Member findMember = memberFinder.findMember(loginMember);

        if (reservationRepository.existsByDateAndThemeAndTimeAndMember(request.date(), theme, time, findMember))
            throw new CustomAlreadyReservationException(ALREADY_EXIST_RESERVATION.getMessage());

        Reservation reservation = reservationRepository.save(new Reservation(findMember, request.date(), time, theme, ReservationStatus.WAITING));

        return new ReservationResponse(reservation.getId(), findMember.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());

    }
}
