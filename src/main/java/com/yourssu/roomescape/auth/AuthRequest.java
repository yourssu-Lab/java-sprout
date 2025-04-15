package com.yourssu.roomescape.auth;

// 기존 LoginRequest를 대체
// ✅ 로그인 전용 DTO 분리 → 응집도 향상
public class AuthRequest {

    private String email;
    private String password;

    public AuthRequest() {}

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}