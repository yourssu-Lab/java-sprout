package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.exception.MemberNotFoundException;
import com.yourssu.roomescape.util.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 로그인 로직: 이메일+비밀번호로 사용자 조회 후, JWT 발급
     */
    public AuthResponse login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .orElseThrow(() -> new MemberNotFoundException("이메일 또는 비밀번호가 틀렸습니다."));

        String token = jwtTokenProvider.createToken(member);
        return new AuthResponse(token);
    }

    /**
     * 토큰 기반 사용자 정보 조회: 이메일 기반 조회
     */
    public String loginCheck(String token) {
        String memberEmail = jwtTokenProvider.getEmail(token);
        Member member = memberDao.findByEmail(memberEmail)
                .orElseThrow(() -> new MemberNotFoundException("로그인된 사용자를 찾을 수 없습니다."));
        return member.getName();
    }
}
