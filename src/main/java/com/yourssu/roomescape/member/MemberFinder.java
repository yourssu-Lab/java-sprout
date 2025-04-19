package com.yourssu.roomescape.member;

import org.springframework.stereotype.Component;

import static com.yourssu.roomescape.exception.ExceptionMessage.NO_EXIST_MEMBER;

@Component
public class MemberFinder {

    private final MemberRepository memberRepository;


    public MemberFinder(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMember(LoginMember loginMember){
        return memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_MEMBER.getMessage()));
    }
}
