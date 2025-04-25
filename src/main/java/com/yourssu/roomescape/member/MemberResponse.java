package com.yourssu.roomescape.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private final Long id;
    private final String name;
    private final String email;

}
