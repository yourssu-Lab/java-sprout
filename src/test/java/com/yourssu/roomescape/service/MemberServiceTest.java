package com.yourssu.roomescape.service;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.member.MemberRole;
import com.yourssu.roomescape.member.MemberService;
import com.yourssu.roomescape.member.dto.MemberRequest;
import com.yourssu.roomescape.member.dto.MemberResponse;
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
class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 생성 성공")
    void createMember_success() {
        MemberRequest request = new MemberRequest("brown", "brown@email.com", "password");

        MemberResponse response = memberService.createMember(request);

        assertNotNull(response.id());
        assertEquals("brown", response.name());
        assertEquals("brown@email.com", response.email());
    }

    @Test
    @DisplayName("회원 생성 실패 - 이메일 중복")
    void createMember_fail_duplicateEmail() {
        memberRepository.save(new Member("brown", "brown@email.com", "password", MemberRole.USER));

        MemberRequest request = new MemberRequest("brown2", "brown@email.com", "password2");

        assertThatThrownBy(() -> memberService.createMember(request))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
