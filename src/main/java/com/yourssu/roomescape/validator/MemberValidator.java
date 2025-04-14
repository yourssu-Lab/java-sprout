package com.yourssu.roomescape.validator;

import com.yourssu.roomescape.member.Member;

import static com.yourssu.roomescape.exception.ExceptionMessage.NO_EXIST_MEMBER;

public class MemberValidator {

    public static void validateMemeber(Member member){
        if (member == null){
            throw new IllegalArgumentException(NO_EXIST_MEMBER.getMessage());
        }
    }
}
