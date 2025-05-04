package com.yourssu.roomescape.service;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.reservation.Reservation;
import com.yourssu.roomescape.reservation.ReservationRepository;
import com.yourssu.roomescape.reservation.ReservationStatus;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.theme.ThemeService;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ThemeServiceTest {

    @Autowired private ThemeService themeService;
    @Autowired private ThemeRepository themeRepository;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private TimeRepository timeRepository;

    private Theme theme;
    private Member member;
    private Time time;

    @BeforeEach
    void setUp() {
        theme = themeRepository.save(new Theme("Theme A", "Description"));
        member = memberRepository.save(new Member("user", "user@email.com", "pw123", null));
        time = timeRepository.save(new Time("10:00"));
    }

    @Test
    @DisplayName("테마 생성 성공")
    void save_success() {
        Theme saved = themeService.save(new Theme("Theme B", "Desc"));
        assertNotNull(saved.getId());
        assertEquals("Theme B", saved.getName());
    }

    @Test
    @DisplayName("테마 생성 실패 - 중복")
    void save_fail_duplicate() {
        assertThatThrownBy(() -> themeService.save(new Theme("Theme A", "Desc")))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.THEME_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("테마 전체 조회")
    void findAll_success() {
        List<Theme> themes = themeService.findAll();
        assertEquals(1, themes.size());
    }

    @Test
    @DisplayName("테마 삭제 성공")
    void delete_success() {
        Theme newTheme = themeRepository.save(new Theme("Theme B", "Desc"));
        themeService.deleteById(newTheme.getId());
        assertFalse(themeRepository.existsById(newTheme.getId()));
    }

    @Test
    @DisplayName("테마 삭제 실패 - 예약 존재")
    void delete_fail_themeInUse() {
        reservationRepository.save(new Reservation(member, "2025-05-03", time, theme, ReservationStatus.RESERVED));

        assertThatThrownBy(() -> themeService.deleteById(theme.getId()))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.THEME_IN_USE);
    }
}