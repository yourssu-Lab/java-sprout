package com.yourssu.roomescape.member;

import com.fasterxml.jackson.annotation.JsonProperty;

// 클래스
public class MemberRequest {
    private String name;
    private String email;
    private String password;

    // 생성자
    public MemberRequest(){}

    // 메서드
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }
}
