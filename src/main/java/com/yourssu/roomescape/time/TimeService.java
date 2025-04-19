package com.yourssu.roomescape.time;

import com.yourssu.roomescape.reservation.Reservation;
import com.yourssu.roomescape.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeService {
    private final TimeDao timeDao;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeDao timeDao, ReservationRepository reservationRepository) {
        this.timeDao = timeDao;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<Time> times = timeDao.findAll();

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
        return timeDao.findAll();
    }

    public Time save(Time time) {
        return timeDao.save(time);
    }

    public void deleteById(Long id) {
        timeDao.deleteById(id);
    }
}
