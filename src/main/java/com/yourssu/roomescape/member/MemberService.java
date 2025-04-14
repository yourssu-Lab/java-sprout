package com.yourssu.roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberDao memberDao;

    // 생성자
    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password);
    }

    public String getMemberName(Long memberId){
        Member member  =  memberDao.findByMemberId(memberId);
        return member.getName();
    }

    // MemberService
    public MemberResponse findMemberById(Long memberId) {
        Member member = memberDao.findByMemberId(memberId);
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }

    public Member findByName(String name) {
        return memberDao.findByName(name); // DB에서 조회
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

    public Member findByMemberId(Long memberId) {
        return memberDao.findByMemberId(memberId);
    }
}