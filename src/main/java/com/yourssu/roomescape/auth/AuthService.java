package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.exception.MemberNotFoundException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.MemberRepository;
import com.yourssu.roomescape.util.JwtTokenProvider;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse login(AuthRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.INVALID_LOGIN, "이메일: " + request.email()));
        String token = jwtTokenProvider.createToken(member);
        return new AuthResponse(token);
    }

    public String getNameFromToken(String token) {
        String email = jwtTokenProvider.getEmail(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND, "이메일: " + email));
        return member.getName();
    }
}
