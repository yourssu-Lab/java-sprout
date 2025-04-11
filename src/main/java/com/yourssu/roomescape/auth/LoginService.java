package com.yourssu.roomescape.auth;

import com.yourssu.roomescape.infrastructure.JwtTokenProvider;
import com.yourssu.roomescape.member.Member;
import com.yourssu.roomescape.member.MemberDao;
import com.yourssu.roomescape.member.MemberService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;


    public LoginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        return jwtTokenProvider.createToken(member);
    }

    public String checkLogin(String token) {
        String memberEmail = jwtTokenProvider.getPayload(token);
        Member member = memberDao.findByEmail(memberEmail);
        return member.getName();
    }
}
