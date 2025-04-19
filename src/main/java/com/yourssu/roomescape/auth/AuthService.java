package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.jwt.TokenProvider;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        return tokenProvider.createToken(member.getEmail());
    }

    public Member checkLogin(String token) {
        String payload = tokenProvider.getPayload(token);
        return memberRepository.findByEmail(payload)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
