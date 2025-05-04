package com.yourssu.roomescape.waiting;

import com.yourssu.roomescape.common.exception.ResourceNotFoundException;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.reservation.ReservationRepository;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
import com.yourssu.roomescape.time.Time;
import com.yourssu.roomescape.time.TimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingServiceTest {

    @Mock
    private WaitingRepository waitingRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private TimeRepository timeRepository;

    @InjectMocks
    private WaitingService waitingService;

    private Member testMember;
    private Theme testTheme;
    private Time testTime;
    private Waiting testWaiting;
    private String testDate;
    private Long memberId, themeId, timeId;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        themeId = 1L;
        timeId = 1L;
        testDate = "2025-05-04";

        testMember = new Member(memberId, "테스트유저", "test@example.com", "USER");
        testTheme = new Theme(themeId, "테스트테마", "테스트 테마 설명");
        testTime = new Time(timeId, "10:00");
        testWaiting = new Waiting(testDate, testTime, testTheme, testMember);

        // ID 설정 (리플렉션 필요없이 간단히 처리)
        try {
            java.lang.reflect.Field idField = Waiting.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testWaiting, 1L);
        } catch (Exception e) {
            // 예외 무시
        }
    }

    @Test
    @DisplayName("대기 추가 - 성공 케이스")
    void addWaitingByMemberId_Success() {
        // Given
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));
        when(themeRepository.findById(themeId)).thenReturn(Optional.of(testTheme));
        when(timeRepository.findById(timeId)).thenReturn(Optional.of(testTime));
        when(reservationRepository.existsByMemberIdAndDateAndThemeIdAndTimeId(
                memberId, testDate, themeId, timeId)).thenReturn(false);
        when(waitingRepository.existsByMember_IdAndDateAndTheme_IdAndTime_Id(
                memberId, testDate, themeId, timeId)).thenReturn(false);
        when(waitingRepository.save(any(Waiting.class))).thenReturn(testWaiting);
        when(waitingRepository.findByDateAndThemeIdAndTimeId(testDate, themeId, timeId))
                .thenReturn(Collections.singletonList(testWaiting));

        // When
        WaitingResponse result = waitingService.addWaitingByMemberId(testDate, themeId, timeId, memberId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTheme()).isEqualTo(testTheme.getName());
        assertThat(result.getDate()).isEqualTo(testDate);
        assertThat(result.getTime()).isEqualTo(testTime.getValue());
        assertThat(result.getWaitingNumber()).isEqualTo(1L);

        verify(waitingRepository, times(1)).save(any(Waiting.class));
        verify(waitingRepository, times(1)).findByDateAndThemeIdAndTimeId(testDate, themeId, timeId);
    }

    @Test
    @DisplayName("대기 추가 - 사용자 찾을 수 없음")
    void addWaitingByMemberId_MemberNotFound() {
        // Given
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> waitingService.addWaitingByMemberId(testDate, themeId, timeId, memberId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Member not found");
    }

    @Test
    @DisplayName("대기 추가 - 이미 예약됨")
    void addWaitingByMemberId_AlreadyReserved() {
        // Given
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));
        when(reservationRepository.existsByMemberIdAndDateAndThemeIdAndTimeId(
                memberId, testDate, themeId, timeId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> waitingService.addWaitingByMemberId(testDate, themeId, timeId, memberId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 예약한 시간입니다");
    }

    @Test
    @DisplayName("대기 추가 - 이미 대기 중")
    void addWaitingByMemberId_AlreadyWaiting() {
        // Given
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));
        when(reservationRepository.existsByMemberIdAndDateAndThemeIdAndTimeId(
                memberId, testDate, themeId, timeId)).thenReturn(false);
        when(waitingRepository.existsByMember_IdAndDateAndTheme_IdAndTime_Id(
                memberId, testDate, themeId, timeId)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> waitingService.addWaitingByMemberId(testDate, themeId, timeId, memberId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 대기 중인 시간입니다");
    }

    @Test
    @DisplayName("대기 추가 - 대기 순서 계산")
    void addWaitingByMemberId_CalculatesWaitingNumber() {
        // Given
        Waiting waiting1 = new Waiting(testDate, testTime, testTheme, testMember);
        Waiting waiting2 = new Waiting(testDate, testTime, testTheme, testMember);

        // ID 설정
        try {
            java.lang.reflect.Field idField = Waiting.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(waiting1, 1L);
            idField.set(waiting2, 2L);
        } catch (Exception e) {
            // 예외 무시
        }

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));
        when(themeRepository.findById(themeId)).thenReturn(Optional.of(testTheme));
        when(timeRepository.findById(timeId)).thenReturn(Optional.of(testTime));
        when(reservationRepository.existsByMemberIdAndDateAndThemeIdAndTimeId(
                memberId, testDate, themeId, timeId)).thenReturn(false);
        when(waitingRepository.existsByMember_IdAndDateAndTheme_IdAndTime_Id(
                memberId, testDate, themeId, timeId)).thenReturn(false);
        when(waitingRepository.save(any(Waiting.class))).thenReturn(waiting2);
        when(waitingRepository.findByDateAndThemeIdAndTimeId(testDate, themeId, timeId))
                .thenReturn(Arrays.asList(waiting1, waiting2));

        // When
        WaitingResponse result = waitingService.addWaitingByMemberId(testDate, themeId, timeId, memberId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getWaitingNumber()).isEqualTo(2L); // 대기 순서는 2
    }

    @Test
    @DisplayName("대기 삭제")
    void deleteWaiting_ShouldCallRepositoryDeleteById() {
        // Given
        Long waitingId = 1L;
        doNothing().when(waitingRepository).deleteById(waitingId);

        // When
        waitingService.deleteWaiting(waitingId);

        // Then
        verify(waitingRepository, times(1)).deleteById(waitingId);
    }
}
