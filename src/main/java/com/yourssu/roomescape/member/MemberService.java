package com.yourssu.roomescape.member;

import com.yourssu.roomescape.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = new Member(request.getName(), request.getEmail(), request.getPassword(), "USER");
        Member saved = memberRepository.save(member);
        return new MemberResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

}
