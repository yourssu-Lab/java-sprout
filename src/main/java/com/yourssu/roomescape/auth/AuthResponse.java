package com.yourssu.roomescape.auth;

// 기존 LoginResponse 대신 사용
// ✅ 응답 DTO도 역할별로 분리 → 단일 책임 원칙 (SRP)
public class AuthResponse {

    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}