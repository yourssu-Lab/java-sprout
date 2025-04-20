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

    public AuthResponse login(AuthRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new MemberNotFoundException("이메일 또는 비밀번호가 틀렸습니다."));
        String token = jwtTokenProvider.createToken(member);
        return new AuthResponse(token);
    }

    public String getNameFromToken(String token) {
        String email = jwtTokenProvider.getEmail(token);
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("토큰에 해당하는 사용자를 찾을 수 없습니다."));
        return member.getName();
    }
}