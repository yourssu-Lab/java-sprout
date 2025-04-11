package com.yourssu.roomescape.member;

import com.yourssu.roomescape.utils.TokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import static com.yourssu.roomescape.exception.ExceptionMessage.NO_EXIST_MEMBER;

@Service
public class MemberService {
    private MemberDao memberDao;
    private final TokenUtil tokenUtil = new TokenUtil();

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginPostRequest request){
        Member member = memberDao.findByEmailAndPassword(request.getEmail(), request.getPassword());

        if (member == null){
            throw new IllegalArgumentException(NO_EXIST_MEMBER.getMessage());
        }

        return tokenUtil.createToken(member);
    }

    public MemberResponse checkLogin(String token){

        Long memberId = tokenUtil.parseTokenToId(token);

        Member findMember = memberDao.findById(memberId);
        return new MemberResponse(findMember.getId(), findMember.getName(), findMember.getEmail());
    }
}
