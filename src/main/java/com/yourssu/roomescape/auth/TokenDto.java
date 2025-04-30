package com.yourssu.roomescape.auth;

public class TokenDto {

    private String token;

    public TokenDto(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
