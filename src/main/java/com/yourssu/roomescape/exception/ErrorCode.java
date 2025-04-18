package com.yourssu.roomescape.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 생겼습니다."),
	COOKIE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "쿠키가 존재하지 않습니다."),
	TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "쿠키에 토큰이 존재하지 않습니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 정보를 찾을 수 없습니다."),
	INVALID_RESERVATION_REQUEST(HttpStatus.BAD_REQUEST, "예약 요청 값이 누락되었습니다."),
	UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "해당 접근에 대한 권한이 없습니다."),
	DUPLICATE_MEMBER_NAME_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, "중복된 이름이 존재합니다. 관리자에게 문의하세요.");

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
