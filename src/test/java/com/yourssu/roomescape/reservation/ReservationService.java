package com.yourssu.roomescape.reservation;

import com.yourssu.roomescape.auth.UserInfo;
import com.yourssu.roomescape.common.exception.UnauthorizedException;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import com.yourssu.roomescape.waiting.WaitingRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WaitingRepository waitingRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Member testMember;
    private Theme testTheme;
    private Time testTime;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        testMember = new Member(1L, "테스트유저", "test@example.com", "USER");
        testTheme = new Theme(1L, "테스트테마", "테스트 테마 설명");
        testTime = new Time(1L, "10:00");
        testReservation = new Reservation("2025-05-04", testTime, testTheme, testMember);
    }

    @Test
    @DisplayName("모든 예약 조회")
    void findAll_ShouldReturnAllReservations() {
        // Given
        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(testReservation));

        // When
        List<ReservationResponse> result = reservationService.findAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo(testMember.getName());
        assertThat(result.get(0).theme()).isEqualTo(testTheme.getName());
        assertThat(result.get(0).date()).isEqualTo("2025-05-04");
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("예약 생성 - 로그인한 사용자")
    void save_WithLoggedInUser_ShouldCreateNewReservation() {
        // Given
        ReservationRequest request = new ReservationRequest(null, "2025-05-04", 1L, 1L);

        when(memberRepository.findById(testMember.getId())).thenReturn(Optional.of(testMember));
        when(themeRepository.findById(1L)).thenReturn(Optional.of(testTheme));
        when(timeRepository.findById(1L)).thenReturn(Optional.of(testTime));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        // When
        ReservationResponse result = reservationService.save(request, testMember);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(testMember.getName());
        assertThat(result.theme()).isEqualTo(testTheme.getName());
        assertThat(result.date()).isEqualTo("2025-05-04");
        assertThat(result.time()).isEqualTo(testTime.getValue());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("예약 생성 - 비회원은 예약 불가")
    void save_WithoutUserInfo_ShouldThrowException() {
        // Given
        ReservationRequest request = new ReservationRequest(null, "2025-05-04", 1L, 1L);

        // When & Then
        assertThatThrownBy(() -> reservationService.save(request, null))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("내 예약 조회")
    void findMyReservations_ShouldReturnUserReservations() {
        // Given
        when(reservationRepository.findByMember(testMember)).thenReturn(Collections.singletonList(testReservation));
        when(waitingRepository.findWaitingsWithRankByMemberId(testMember.getId())).thenReturn(Collections.emptyList());

        // When
        List<MyReservationResponse> result = reservationService.findMyReservations(testMember);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).theme()).isEqualTo(testTheme.getName());
        assertThat(result.get(0).date()).isEqualTo("2025-05-04");
        assertThat(result.get(0).status()).isEqualTo("예약");
        verify(reservationRepository, times(1)).findByMember(testMember);
    }

    @Test
    @DisplayName("예약 삭제")
    void deleteById_ShouldDeleteReservation() {
        // Given
        Long reservationId = 1L;
        doNothing().when(reservationRepository).deleteById(reservationId);

        // When
        reservationService.deleteById(reservationId);

        // Then
        verify(reservationRepository, times(1)).deleteById(reservationId);
    }
}
