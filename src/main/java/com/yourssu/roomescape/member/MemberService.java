package com.yourssu.roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = new Member(request.name(), request.email(), request.password(), "USER");
        Member saved = memberRepository.save(member);
        return new MemberResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

}
