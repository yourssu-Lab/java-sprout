package com.yourssu.roomescape.exception;

// ✅ 일관된 에러 응답 포맷 제공 (전역 예외 처리기에서 사용)
public class ErrorResponse {

    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}