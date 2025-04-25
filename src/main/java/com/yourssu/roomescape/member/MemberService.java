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

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("이메일이 일치하는 회원이 없습니다."));
    }

    public Member findByName(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new MemberNotFoundException("이름이 일치하는 회원이 없습니다."));
    }
}
