package com.yourssu.roomescape.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMember {
    private final Long id;
    private final String name;
    private final String email;
    private final String role;

}
