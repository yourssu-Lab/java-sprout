package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.infrastructure.JwtTokenProvider;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        return jwtTokenProvider.createToken(member);
    }

    public String checkLogin(String token) {
        String memberEmail = jwtTokenProvider.getPayload(token);
        Member member = memberRepository.findByEmail(memberEmail);
        return member.getName();
    }
}
