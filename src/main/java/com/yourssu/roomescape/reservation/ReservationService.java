package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeDao;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final ThemeDao themeDao;
    private final TimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao, ThemeDao themeDao, TimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.themeDao = themeDao;
        this.timeDao = timeDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Member member) {
        String memberName = reservationRequest.getName();

        if (memberName != null) {
            Member existingMember = memberDao.findByName(memberName)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            memberName = existingMember.getName();
        } else {
            memberName = member.getName();
        }

        Time time = timeDao.findById(reservationRequest.getTime());
        Theme theme = themeDao.findById(reservationRequest.getTheme());

        Reservation reservation = new Reservation(
                memberName,
                reservationRequest.getDate(),
                time,
                theme
        );

        Reservation newReservation = reservationDao.save(reservation);

        return new ReservationResponse(
                newReservation.getId(),
                newReservation.getName(),
                newReservation.getTheme().getName(),
                newReservation.getDate(),
                newReservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
