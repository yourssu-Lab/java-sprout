package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.jwt.TokenProvider;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;

    public AuthService(TokenProvider tokenProvider, MemberDao memberDao) {
        this.tokenProvider = tokenProvider;
        this.memberDao = memberDao;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        return tokenProvider.createToken(member.getEmail());
    }

    public CheckLoginResponse checkLogin(String token) {
        String payload = tokenProvider.getPayload(token);
        Member member = memberDao.findByEmail(payload);
        return new CheckLoginResponse(member.getName());
    }
}
