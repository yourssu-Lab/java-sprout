package com.yourssu.roomescape.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 생겼습니다."),

    // Auth
    COOKIE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "쿠키가 존재하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "쿠키에 토큰이 존재하지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰 형식입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "손상된 JWT 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 정보를 찾을 수 없습니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 틀렸습니다."),
    NOT_ADMIN(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),

    // Reservation
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다."),
    NO_PERMISSION_FOR_RESERVATION(HttpStatus.FORBIDDEN, "해당 예약에 대한 권한이 없습니다."),
    RESERVATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "예약 정보가 이미 존재합니다."),
    WAITING_ALREADY_EXISTS(HttpStatus.CONFLICT, "예약 대기 정보가 이미 존재합니다."),

    // Time
    TIME_NOT_FOUND(HttpStatus.NOT_FOUND, "시간 정보를 찾을 수 없습니다."),

    // Theme
    THEME_NOT_FOUND(HttpStatus.NOT_FOUND, "테마 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
