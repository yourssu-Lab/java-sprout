package com.yourssu.roomescape.auth;

public class LoginCheckResponse {
    private String name;

    public LoginCheckResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
