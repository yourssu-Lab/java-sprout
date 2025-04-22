package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.reservation.dto.AdminReservationResponse;
import com.yourssu.roomescape.reservation.dto.ReservationResponse;
import com.yourssu.roomescape.reservation.dto.UserReservationResponse;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Member member) {
        Time time = timeRepository.findById(reservationRequest.getTime()).orElse(null);
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElse(null);

        String name;
        if (reservationRequest.getName() == null) {
            name = member.getName();
        } else {
            name = reservationRequest.getName();
        }

        Reservation reservation = reservationRepository.save(
                new Reservation(name, member, reservationRequest.getDate(), time, theme));
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<AdminReservationResponse> findReservationsByAdmin(Member member) {
        return reservationRepository.findAllByMemberId(member.getId()).stream()
                .map(it -> new AdminReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
    public List<UserReservationResponse> findReservationsByUser(Member member) {
        return reservationRepository.findAllByMemberId(member.getId()).stream()
                .map(it -> new UserReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
