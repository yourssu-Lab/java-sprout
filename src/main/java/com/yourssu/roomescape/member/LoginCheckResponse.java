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


// 객체 생성의 정석
// 변수 생성자 게터