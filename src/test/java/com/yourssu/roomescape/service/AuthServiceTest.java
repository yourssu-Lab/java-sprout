package com.yourssu.roomescape.service;

import com.yourssu.roomescape.auth.AuthService;
import com.yourssu.roomescape.auth.dto.LoginRequest;
import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.jwt.TokenProvider;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.member.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired  private AuthService authService;
    @Autowired  private TokenProvider tokenProvider;
    @Autowired  private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member("user", "user@email.com", "pw123", MemberRole.USER));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        LoginRequest request = new LoginRequest("user@email.com", "pw123");

        String token = authService.login(request);
        String email = tokenProvider.getPayload(token);

        assertNotNull(token);
        assertEquals("user@email.com", email);
    }

    @Test
    @DisplayName("로그인 실패 - 이메일/비밀번호 불일치")
    void login_fail_invalidCredentials() {
        LoginRequest request = new LoginRequest("user@email.com", "pw456");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("토큰 검증 성공")
    void checkLogin_success() {
        String token = tokenProvider.createToken("user@email.com");

        Member member = authService.checkLogin(token);

        assertEquals("user@email.com", member.getEmail());
        assertEquals("user", member.getName());
    }

    @Test
    @DisplayName("토큰 검증 실패 - 존재하지 않는 사용자")
    void checkLogin_fail_memberNotFound() {
        String token = tokenProvider.createToken("user2@email.com");

        assertThatThrownBy(() -> authService.checkLogin(token))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MEMBER_NOT_FOUND);
    }
}
