package com.yourssu.roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 생겼습니다."),

	// Auth
	COOKIE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "쿠키가 존재하지 않습니다."),
	TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "쿠키에 토큰이 존재하지 않습니다."),

	// Member
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 정보를 찾을 수 없습니다."),
	NOT_ADMIN(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),

	// Reservation
	RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다."),
	NO_PERMISSION_FOR_RESERVATION(HttpStatus.FORBIDDEN, "해당 예약에 대한 권한이 없습니다."),

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

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
