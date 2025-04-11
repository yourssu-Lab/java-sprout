package com.yourssu.roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberDao memberDao;

    // 생성자
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    // 메서드
    public Member findByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password);
    }

    public String getMemberName(Long memberId){
        Member member  =  memberDao.findByMemberId(memberId);
        return member.getName();
    }


    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }

    public MemberResponse login(MemberRequest memberRequest) {
        Member member = memberDao.findByEmailAndPassword(memberRequest.getEmail(), memberRequest.getPassword());
        if (member.getId() == null) {
            throw new IllegalArgumentException("회원 ID가 존재하지 않습니다.");
        }
        return new MemberResponse(member);
    }
}