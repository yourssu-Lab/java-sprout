package com.yourssu.roomescape.member;

public class LoginCheckResponse {
    private String  name;

    public LoginCheckResponse(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
