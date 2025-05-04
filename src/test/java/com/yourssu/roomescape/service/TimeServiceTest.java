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
import com.yourssu.roomescape.time.AvailableTime;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import com.yourssu.roomescape.time.TimeService;
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
class TimeServiceTest {

    @Autowired private TimeService timeService;
    @Autowired private TimeRepository timeRepository;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ThemeRepository themeRepository;

    private Theme theme;
    private Time time1;
    private Time time2;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(new Member("user", "user@email.com", "pw123", null));
        theme = themeRepository.save(new Theme("Theme A", "desc"));

        time1 = timeRepository.save(new Time("10:00"));
        time2 = timeRepository.save(new Time("12:00"));

        reservationRepository.save(new Reservation(member, "2025-05-03", time1, theme, ReservationStatus.RESERVED));
    }

    @Test
    @DisplayName("날짜/테마별 전체 시간 조회 (예약 여부 포함)")
    void getAvailableTime() {
        List<AvailableTime> result = timeService.getAvailableTime("2025-05-03", theme.getId());

        assertEquals(2, result.size());

        AvailableTime t1 = result.stream().filter(t -> t.getTimeId().equals(time1.getId())).findFirst().orElseThrow();
        AvailableTime t2 = result.stream().filter(t -> t.getTimeId().equals(time2.getId())).findFirst().orElseThrow();

        assertTrue(t1.isBooked());
        assertFalse(t2.isBooked());
    }

    @Test
    @DisplayName("시간 전체 조회")
    void findAll() {
        List<Time> result = timeService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("시간 생성 성공")
    void save_success() {
        Time newTime = new Time("14:00");

        Time saved = timeService.save(newTime);

        assertNotNull(saved.getId());
        assertEquals("14:00", saved.getValue());
    }

    @Test
    @DisplayName("시간 생성 실패 - 중복")
    void save_fail_duplicate() {
        assertThatThrownBy(() -> timeService.save(new Time("10:00")))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.TIME_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("시간 삭제 성공")
    void delete_success() {
        Time newTime = timeRepository.save(new Time("15:00"));

        timeService.deleteById(newTime.getId());

        assertFalse(timeRepository.existsById(newTime.getId()));
    }

    @Test
    @DisplayName("시간 삭제 실패 - 예약에 사용 중")
    void delete_fail_inUse() {
        assertThatThrownBy(() -> timeService.deleteById(time1.getId()))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.TIME_IN_USE);
    }
}
