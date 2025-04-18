package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
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

        ReservationRequest updatedRequest = new ReservationRequest(
                memberName,
                reservationRequest.getDate(),
                reservationRequest.getTheme(),
                reservationRequest.getTime()
        );

        Reservation reservation = reservationDao.save(updatedRequest);

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue());
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
