package com.yourssu.roomescape.member;

import com.yourssu.roomescape.exception.CustomException;
import com.yourssu.roomescape.exception.ErrorCode;
import com.yourssu.roomescape.member.dto.MemberRequest;
import com.yourssu.roomescape.member.dto.MemberResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {

        if(memberRepository.existsByEmail(memberRequest.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
