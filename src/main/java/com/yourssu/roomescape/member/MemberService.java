package com.yourssu.roomescape.member;

import com.yourssu.roomescape.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword()); // TODO: 유저 찾기 여기 유저 없으면 예외 처리 어떻게 할지 고민해보기
        String token = jwtTokenProvider.createToken(member.getEmail());
        return new LoginResponse(token);
    }

    public String loginCheck(String token) {

        String email = jwtTokenProvider.getPayload(token);
        Member member = memberDao.findByEmail(email);
        return member.getName();
    }
}
