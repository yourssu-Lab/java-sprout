package com.yourssu.roomescape.service;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.member.MemberRole;
import com.yourssu.roomescape.reservation.Reservation;
import com.yourssu.roomescape.reservation.ReservationRepository;
import com.yourssu.roomescape.reservation.ReservationService;
import com.yourssu.roomescape.reservation.ReservationStatus;
import com.yourssu.roomescape.reservation.dto.ReservationFindAllForAdminResponse;
import com.yourssu.roomescape.reservation.dto.ReservationFindAllResponse;
import com.yourssu.roomescape.reservation.dto.ReservationSaveRequest;
import com.yourssu.roomescape.reservation.dto.ReservationSaveResponse;
import com.yourssu.roomescape.theme.Theme;
import com.yourssu.roomescape.theme.ThemeRepository;
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
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ThemeRepository themeRepository;
    @Autowired private TimeRepository timeRepository;
    @Autowired private ReservationRepository reservationRepository;

    private Member user;
    private Member admin;
    private Theme theme;
    private Time time;

    @BeforeEach
    void setUp() {
        user = memberRepository.save(new Member("user", "user@example.com", "password", MemberRole.USER));
        admin = memberRepository.save(new Member("admin", "admin@example.com", "password", MemberRole.ADMIN));
        theme = themeRepository.save(new Theme("테마1", "테마1입니다."));
        time = timeRepository.save(new Time("10:00"));
    }

    @Test
    @DisplayName("예약 생성 성공")
    void saveTest_success() {
        ReservationSaveRequest request = new ReservationSaveRequest(
                null, "2025-05-03", time.getId(), theme.getId(), ReservationStatus.RESERVED
        );

        ReservationSaveResponse response = reservationService.save(request, user);

        assertNotNull(response.id());
        assertEquals("user", response.name());
    }

    @Test
    @DisplayName("예약 생성 실패 - 중복 예약 불가")
    void saveTest_fail_duplicateReservation() {
        reservationRepository.save(new Reservation(user, "2025-05-03", time, theme, ReservationStatus.RESERVED));

        ReservationSaveRequest request = new ReservationSaveRequest(
                null, "2025-05-03", time.getId(), theme.getId(), ReservationStatus.RESERVED
        );

        assertThatThrownBy(() -> reservationService.save(request, user))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.RESERVATION_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("예약 생성 실패 - 예약 없는 슬롯에 대기 등록 불가")
    void saveTest_fail_waitingWithoutReservation() {
        ReservationSaveRequest request = new ReservationSaveRequest(
                null, "2025-05-03", time.getId(), theme.getId(), ReservationStatus.WAITING
        );

        assertThatThrownBy(() -> reservationService.save(request, user))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CANNOT_WAIT_WITHOUT_RESERVED);
    }

    @Test
    @DisplayName("예약 삭제 성공 - 본인")
    void deleteById_success_byOwner() {
        Reservation reservation = reservationRepository.save(new Reservation(user, "2025-05-03", time, theme, ReservationStatus.RESERVED));

        reservationService.deleteById(reservation.getId(), user);

        assertFalse(reservationRepository.existsById(reservation.getId()));
    }

    @Test
    @DisplayName("예약 삭제 성공 - 관리자")
    void deleteById_success_byAdmin() {
        Reservation reservation = reservationRepository.save(new Reservation(user, "2025-05-03", time, theme, ReservationStatus.RESERVED));

        reservationService.deleteById(reservation.getId(), admin);

        assertFalse(reservationRepository.existsById(reservation.getId()));
    }

    @Test
    @DisplayName("예약 삭제 실패 - 다른 사용자(권한 없음)")
    void deleteById_fail_byOtherUser() {
        Member other = memberRepository.save(new Member("other", "other@example.com", "password", MemberRole.USER));
        Reservation reservation = reservationRepository.save(new Reservation(user, "2025-05-03", time, theme, ReservationStatus.RESERVED));

        assertThatThrownBy(() -> reservationService.deleteById(reservation.getId(), other))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NO_PERMISSION_FOR_RESERVATION);
    }

    @Test
    @DisplayName("본인 예약 목록 조회 성공")
    void getMyReservations_success() {
        reservationRepository.save(new Reservation(user, "2025-05-03", time, theme, ReservationStatus.RESERVED));
        reservationRepository.save(new Reservation(user, "2025-05-04", time, theme, ReservationStatus.WAITING));

        List<ReservationFindAllResponse> result = reservationService.getMyReservations(user);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("전체 예약 목록 조회 성공 - 관리자")
    void getAllReservations_success() {
        reservationRepository.save(new Reservation(user, "2025-05-03", time, theme, ReservationStatus.RESERVED));
        reservationRepository.save(new Reservation(user, "2025-05-04", time, theme, ReservationStatus.WAITING));
        reservationRepository.save(new Reservation(admin, "2025-05-03", time, theme, ReservationStatus.RESERVED));
        reservationRepository.save(new Reservation(admin, "2025-05-04", time, theme, ReservationStatus.WAITING));

        List<ReservationFindAllForAdminResponse> result = reservationService.getAllReservations(admin);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("전체 예약 목록 조회 실패 - 일반 사용자(권한 없음)")
    void getAllReservations_fail_byUser() {
        assertThatThrownBy(() -> reservationService.getAllReservations(user))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_ADMIN);
    }
}
