package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.LoginMember;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getName() == null) {
            Reservation reservation = reservationDao.save(reservationRequest, loginMember.getName());

            return new ReservationResponse(reservation.getId(), loginMember.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
        } else {
            Member member = memberDao.findByName(reservationRequest.getName());
            Reservation reservation = reservationDao.save(reservationRequest, member.getName());

            return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
        }
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
