package com.yourssu.roomescape.common.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        // 테스트용 시크릿 키와 토큰 유효 기간 설정
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "testsecretkeytestsecretkeytestsecretkeytestsecretkey");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L); // 1시간
    }

    @Test
    @DisplayName("JWT 토큰 생성 및 페이로드 추출")
    void createTokenAndGetPayload_ShouldWorkCorrectly() {
        // Given
        String email = "test@example.com";

        // When
        String token = jwtTokenProvider.createToken(email);
        String extractedEmail = jwtTokenProvider.getPayload(token);

        // Then
        assertThat(token).isNotEmpty();
        assertThat(extractedEmail).isEqualTo(email);
    }

    @Test
    @DisplayName("잘못된 JWT 토큰으로 페이로드 추출 시 예외 발생")
    void getPayload_WithInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.token.format";

        // When & Then
        assertThatThrownBy(() -> jwtTokenProvider.getPayload(invalidToken))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("만료된 JWT 토큰으로 페이로드 추출 시 예외 발생")
    void getPayload_WithExpiredToken_ShouldThrowException() {
        // Given
        String email = "test@example.com";

        // 토큰 유효기간을 1ms로 설정하여 만료된 토큰 생성
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 1L);
        String token = jwtTokenProvider.createToken(email);

        // 잠시 대기하여 토큰 만료
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // 무시
        }

        // When & Then
        assertThatThrownBy(() -> jwtTokenProvider.getPayload(token))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("expired");
    }
}
