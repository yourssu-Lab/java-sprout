package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeDao;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeDao themeDao;
    private final TimeDao timeDao;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, ThemeDao themeDao, TimeDao timeDao) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeDao = themeDao;
        this.timeDao = timeDao;
    }

    public ReservationSaveResponse save(ReservationSaveRequest reservationSaveRequest, Member member) {

        Member existingMember;

        if (reservationSaveRequest.getName() != null) {
            existingMember = memberRepository.findByName(reservationSaveRequest.getName())
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        } else {
            existingMember = member;
        }

        Time time = timeDao.findById(reservationSaveRequest.getTime());
        Theme theme = themeDao.findById(reservationSaveRequest.getTheme());

        Reservation reservation = new Reservation(
                existingMember,
                reservationSaveRequest.getDate(),
                time,
                theme
        );

        Reservation newReservation = reservationRepository.save(reservation);

        return new ReservationSaveResponse(
                newReservation.getId(),
                newReservation.getMember().getName(),
                newReservation.getTheme().getName(),
                newReservation.getDate(),
                newReservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationFindAllResponse> findAll(Member member) {
        List<Reservation> reservations = reservationRepository.findByMember(member);

        return reservations.stream()
                .map(reservation -> new ReservationFindAllResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getValue(),
                        "예약"
                ))
                .collect(Collectors.toList());
    }
}
