package com.yourssu.roomescape.member;

import com.yourssu.roomescape.utils.TokenUtil;
import com.yourssu.roomescape.validator.MemberValidator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.yourssu.roomescape.exception.ExceptionMessage.NO_EXIST_MEMBER;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private final TokenUtil tokenUtil;

    public MemberService(MemberRepository memberRepository, TokenUtil tokenUtil) {
        this.memberRepository = memberRepository;
        this.tokenUtil = tokenUtil;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public String login(LoginPostRequest request){
        Member member = memberRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());

        MemberValidator.validateMemeber(member);

        return tokenUtil.createToken(member);
    }

    public MemberResponse checkLogin(String token){
        Long memberId = tokenUtil.parseTokenToId(token);
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_MEMBER.getMessage()));
        return new MemberResponse(findMember.getId(), findMember.getName(), findMember.getEmail(), findMember.getRole()); // 3단계
    }

}
