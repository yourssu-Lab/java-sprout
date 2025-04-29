package com.yourssu.roomescape.member;

import com.yourssu.roomescape.auth.LoginRequest;
import com.yourssu.roomescape.auth.TokenDto;
import com.yourssu.roomescape.common.exception.ResourceNotFoundException;
import com.yourssu.roomescape.common.exception.UnauthorizedException;
import com.yourssu.roomescape.common.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(
                memberRequest.getName(),
                memberRequest.getEmail(),
                memberRequest.getPassword(),
                "USER"
        ));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public TokenDto login(LoginRequest loginRequest) {
        try {
            // 1. 먼저 이메일로만 사용자를 찾고
            Member member = memberRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // 2. 비밀번호 수동 검증
            if (!member.getPassword().equals(loginRequest.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            // 3. 토큰 생성 및 반환
            String token = jwtTokenProvider.createToken(member.getEmail());
            return new TokenDto(token);
        } catch (Exception e) {
            e.printStackTrace(); // 디버깅용
            throw new RuntimeException("Invalid credentials", e);
        }
    }

    public String loginCheck(String token) {

        String email = jwtTokenProvider.getPayload(token);
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        return member.getName();
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
    }

    public Member findByName(String name) {
        return memberRepository.findByName(name).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
    }
}
