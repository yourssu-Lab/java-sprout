package com.yourssu.roomescape.time;

import com.yourssu.roomescape.reservation.Reservation;
import com.yourssu.roomescape.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeService {
    private TimeRepository timeRepositroy;
    private ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepositroy, ReservationRepository reservationRepository) {
        this.timeRepositroy = timeRepositroy;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<Time> times = timeRepositroy.findAll();

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
        return timeRepositroy.findAll();
    }

    public Time save(Time time) {
        return timeRepositroy.save(time);
    }

    public void deleteById(Long id) {
        timeRepositroy.deleteById(id);
    }
}
