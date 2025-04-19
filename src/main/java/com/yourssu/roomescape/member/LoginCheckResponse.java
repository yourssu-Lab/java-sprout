package com.yourssu.roomescape.member;

public class LoginCheckResponse {

    private String name;

    public LoginCheckResponse(String memberName) {
        this.name = memberName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
