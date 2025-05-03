package com.yourssu.roomescape.time;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.reservation.Reservation;
import com.yourssu.roomescape.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;

    public TimeService(ReservationRepository reservationRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<Time> times = timeRepository.findAll();

        return times.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getValue(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public Time save(Time time) {
        return timeRepository.save(time);
    }

    public void deleteById(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new CustomException(ErrorCode.TIME_IN_USE);
        }

        timeRepository.deleteById(id);
    }
}
