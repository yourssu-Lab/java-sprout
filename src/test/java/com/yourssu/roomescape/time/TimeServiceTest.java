package com.yourssu.roomescape.time;

import com.yourssu.roomescape.reservation.Reservation;
import com.yourssu.roomescape.reservation.ReservationRepository;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeServiceTest {

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private TimeService timeService;

    private Time time1, time2;
    private Theme theme;
    private Member member;
    private Reservation reservation;
    private String testDate;

    @BeforeEach
    void setUp() {
        time1 = new Time(1L, "10:00");
        time2 = new Time(2L, "12:00");
        theme = new Theme(1L, "테스트 테마", "테스트 테마 설명");
        member = new Member(1L, "테스트유저", "test@example.com", "USER");
        testDate = "2025-05-04";
        reservation = new Reservation(testDate, time1, theme, member);
    }

    @Test
    @DisplayName("이용 가능한 시간 조회 - 일부 예약됨")
    void getAvailableTime_WithSomeBooked_ShouldReturnTimeWithBookedStatus() {
        // Given
        Long themeId = 1L;

        List<Time> times = Arrays.asList(time1, time2);
        List<Reservation> reservations = Collections.singletonList(reservation);

        when(timeRepository.findAll()).thenReturn(times);
        when(reservationRepository.findByDateAndThemeId(testDate, themeId)).thenReturn(reservations);

        // When
        List<AvailableTime> result = timeService.getAvailableTime(testDate, themeId);

        // Then
        assertThat(result).hasSize(2);

        // 첫 번째 시간은 이미 예약됨
        assertThat(result.get(0).getTimeId()).isEqualTo(1L);
        assertThat(result.get(0).getTime()).isEqualTo("10:00");
        assertThat(result.get(0).isBooked()).isTrue();

        // 두 번째 시간은 예약 가능
        assertThat(result.get(1).getTimeId()).isEqualTo(2L);
        assertThat(result.get(1).getTime()).isEqualTo("12:00");
        assertThat(result.get(1).isBooked()).isFalse();

        verify(timeRepository, times(1)).findAll();
        verify(reservationRepository, times(1)).findByDateAndThemeId(testDate, themeId);
    }

    @Test
    @DisplayName("이용 가능한 시간 조회 - 모두 예약 가능")
    void getAvailableTime_WithNoneBooked_ShouldReturnAllAvailable() {
        // Given
        Long themeId = 1L;

        List<Time> times = Arrays.asList(time1, time2);
        List<Reservation> reservations = Collections.emptyList();

        when(timeRepository.findAll()).thenReturn(times);
        when(reservationRepository.findByDateAndThemeId(testDate, themeId)).thenReturn(reservations);

        // When
        List<AvailableTime> result = timeService.getAvailableTime(testDate, themeId);

        // Then
        assertThat(result).hasSize(2);

        // 모든 시간이 예약 가능함
        assertThat(result.get(0).getTimeId()).isEqualTo(1L);
        assertThat(result.get(0).getTime()).isEqualTo("10:00");
        assertThat(result.get(0).isBooked()).isFalse();

        assertThat(result.get(1).getTimeId()).isEqualTo(2L);
        assertThat(result.get(1).getTime()).isEqualTo("12:00");
        assertThat(result.get(1).isBooked()).isFalse();

        verify(timeRepository, times(1)).findAll();
        verify(reservationRepository, times(1)).findByDateAndThemeId(testDate, themeId);
    }

    @Test
    @DisplayName("모든 시간 조회")
    void findAll_ShouldReturnAllTimes() {
        // Given
        List<Time> times = Arrays.asList(time1, time2);
        when(timeRepository.findAll()).thenReturn(times);

        // When
        List<Time> result = timeService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(time1, time2);
        verify(timeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("시간 추가")
    void save_ShouldSaveAndReturnTime() {
        // Given
        Time newTime = new Time("14:00");
        Time savedTime = new Time(3L, "14:00");

        when(timeRepository.save(any(Time.class))).thenReturn(savedTime);

        // When
        Time result = timeService.save(newTime);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getValue()).isEqualTo("14:00");
        verify(timeRepository, times(1)).save(newTime);
    }

    @Test
    @DisplayName("시간 삭제")
    void deleteById_ShouldCallRepositoryDeleteById() {
        // Given
        Long timeId = 1L;
        doNothing().when(timeRepository).deleteById(timeId);

        // When
        timeService.deleteById(timeId);

        // Then
        verify(timeRepository, times(1)).deleteById(timeId);
    }
}
