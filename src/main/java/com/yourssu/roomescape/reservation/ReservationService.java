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
        return new ReservationResponse(
                reservation.getId(),
                reservationRequest.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        // Create a new ReservationRequest with all the data we need
        ReservationRequestDto finalRequest = new ReservationRequestDto(
                reservationRequest.getName() != null ? reservationRequest.getName() : loginMember.getName(),
                reservationRequest.getDate(),
                reservationRequest.getTheme(),
                reservationRequest.getTime()
        );

        // Use the DAO to save the reservation
        Reservation reservation = reservationDao.save(finalRequest);

        // Return the response
        return new ReservationResponse(
                reservation.getId(),
                finalRequest.getName(),
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

    private static class ReservationRequestDto extends ReservationRequest {
        private final String name;
        private final String date;
        private final Long theme;
        private final Long time;

        public ReservationRequestDto(String name, String date, Long theme, Long time) {
            this.name = name;
            this.date = date;
            this.theme = theme;
            this.time = time;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDate() {
            return date;
        }

        @Override
        public Long getTheme() {
            return theme;
        }

        @Override
        public Long getTime() {
            return time;
        }
    }
}