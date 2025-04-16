package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.LoginMember;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.member.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private MemberService memberService;

    public ReservationService(ReservationDao reservationDao, MemberService memberService) {
        this.reservationDao = reservationDao;
        this.memberService = memberService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.save(reservationRequest);
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        // name이 있으면 그대로 사용하고, 없으면 로그인 멤버의 이름을 사용
        String reservationName = reservationRequest.getName() != null
                ? reservationRequest.getName()
                : loginMember.getName();

        // 기존 ReservationRequest를 확장하여 이름을 설정
        ReservationRequest finalRequest = new ReservationRequest() {
            @Override
            public String getName() {
                return reservationName;
            }

            @Override
            public String getDate() {
                return reservationRequest.getDate();
            }

            @Override
            public Long getTheme() {
                return reservationRequest.getTheme();
            }

            @Override
            public Long getTime() {
                return reservationRequest.getTime();
            }
        };

        // 기존 save 메서드를 호출하여 예약 저장
        Reservation reservation = reservationDao.save(finalRequest);

        return new ReservationResponse(
                reservation.getId(),
                reservationName,
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
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
