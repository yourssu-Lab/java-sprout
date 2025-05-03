package com.yourssu.roomescape.theme;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.reservation.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteById(Long id) {
        if (reservationRepository.existsByThemeId(id)) {
            throw new CustomException(ErrorCode.THEME_IN_USE);
        }

        themeRepository.deleteById(id);
    }
}
