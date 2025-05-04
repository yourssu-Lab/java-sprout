package com.yourssu.roomescape.member;

import com.yourssu.roomescape.auth.LoginRequest;
import com.yourssu.roomescape.auth.TokenDto;
import com.yourssu.roomescape.common.exception.BadRequestException;
import com.yourssu.roomescape.common.exception.ResourceNotFoundException;
import com.yourssu.roomescape.common.exception.UnauthorizedException;
import com.yourssu.roomescape.common.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;
    private MemberRequest testMemberRequest;
    private LoginRequest testLoginRequest;

    @BeforeEach
    void setUp() {
        testMember = new Member(1L, "테스트유저", "test@example.com", "USER");
        testMemberRequest = new MemberRequest("테스트유저", "test@example.com", "password123");

        // LoginRequest 구현
        testLoginRequest = new LoginRequest() {
            @Override
            public String getEmail() {
                return "test@example.com";
            }

            @Override
            public String getPassword() {
                return "password123";
            }
        };
    }

    @Test
    @DisplayName("회원 생성 성공")
    void createMember_Success() {
        // Given
        when(memberRepository.findByEmail(testMemberRequest.email())).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // When
        MemberResponse result = memberService.createMember(testMemberRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(testMember.getId());
        assertThat(result.name()).isEqualTo(testMember.getName());
        assertThat(result.email()).isEqualTo(testMember.getEmail());
    }

    @Test
    @DisplayName("회원 생성 실패 - 이미 존재하는 이메일")
    void createMember_WithExistingEmail_ShouldThrowException() {
        // Given
        when(memberRepository.findByEmail(testMemberRequest.email())).thenReturn(Optional.of(testMember));

        // When & Then
        assertThatThrownBy(() -> memberService.createMember(testMemberRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("이미 사용 중인 이메일입니다");
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // Given
        // 비밀번호 설정
        Member memberWithPassword = new Member(1L, "테스트유저", "test@example.com", "USER");
        try {
            java.lang.reflect.Field passwordField = Member.class.getDeclaredField("password");
            passwordField.setAccessible(true);
            passwordField.set(memberWithPassword, "password123");
        } catch (Exception e) {
            // 예외 무시
        }

        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(memberWithPassword));
        when(jwtTokenProvider.createToken(anyString())).thenReturn("test-token");

        // When
        TokenDto result = memberService.login(testLoginRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("test-token");
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_WithNonExistingEmail_ShouldThrowException() {
        // Given
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.login(testLoginRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_WithWrongPassword_ShouldThrowException() {
        // Given
        // 다른 비밀번호 설정
        Member memberWithWrongPassword = new Member(1L, "테스트유저", "test@example.com", "USER");
        try {
            java.lang.reflect.Field passwordField = Member.class.getDeclaredField("password");
            passwordField.setAccessible(true);
            passwordField.set(memberWithWrongPassword, "wrong-password");
        } catch (Exception e) {
            // 예외 무시
        }

        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(memberWithWrongPassword));

        // When & Then
        assertThatThrownBy(() -> memberService.login(testLoginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid credentials");
    }

    @Test
    @DisplayName("토큰으로 로그인 확인 성공")
    void loginCheck_Success() {
        // Given
        String token = "valid-token";

        when(jwtTokenProvider.getPayload(token)).thenReturn("test@example.com");
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testMember));

        // When
        String result = memberService.loginCheck(token);

        // Then
        assertThat(result).isEqualTo(testMember.getName());
    }

    @Test
    @DisplayName("토큰으로 로그인 확인 실패 - 존재하지 않는 이메일")
    void loginCheck_WithNonExistingEmail_ShouldThrowException() {
        // Given
        String token = "valid-token";

        when(jwtTokenProvider.getPayload(token)).thenReturn("nonexistent@example.com");
        when(memberRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.loginCheck(token))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Invalid credentials");
    }

    @Test
    @DisplayName("이메일로 회원 찾기 성공")
    void findByEmail_Success() {
        // Given
        String email = "test@example.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(testMember));

        // When
        Member result = memberService.findByEmail(email);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testMember.getId());
        assertThat(result.getName()).isEqualTo(testMember.getName());
        assertThat(result.getEmail()).isEqualTo(testMember.getEmail());
    }

    @Test
    @DisplayName("이메일로 회원 찾기 실패 - 존재하지 않는 이메일")
    void findByEmail_WithNonExistingEmail_ShouldThrowException() {
        // Given
        String email = "nonexistent@example.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.findByEmail(email))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Invalid credentials");
    }

    @Test
    @DisplayName("이름으로 회원 찾기 성공")
    void findByName_Success() {
        // Given
        String name = "테스트유저";
        when(memberRepository.findByName(name)).thenReturn(Optional.of(testMember));

        // When
        Member result = memberService.findByName(name);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testMember.getId());
        assertThat(result.getName()).isEqualTo(testMember.getName());
        assertThat(result.getEmail()).isEqualTo(testMember.getEmail());
    }

    @Test
    @DisplayName("이름으로 회원 찾기 실패 - 존재하지 않는 이름")
    void findByName_WithNonExistingName_ShouldThrowException() {
        // Given
        String name = "존재하지않는유저";
        when(memberRepository.findByName(name)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberService.findByName(name))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Invalid credentials");
    }
}
